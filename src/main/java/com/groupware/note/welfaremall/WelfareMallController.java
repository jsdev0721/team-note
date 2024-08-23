package com.groupware.note.welfaremall;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.groupware.note.files.FileService;
import com.groupware.note.files.Files;
import com.groupware.note.user.UserService;
import com.groupware.note.user.Users;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/welfaremall")
public class WelfareMallController {
	private final WelfareMallService welfareMallService;
	private final CartService cartService;
	private final FileService fileService;
	private final UserService userService;
	private final PurchaseService purchaseService;
	
	@GetMapping("/list")
	public String findAll(Model model , @RequestParam(value = "page" , defaultValue = "0") int page ,@RequestParam(defaultValue = "personal" , value = "type")String type) {
		model.addAttribute("list", this.welfareMallService.findAll(type , page, 9));
		return "welfaremallList";
	}
	
	@PostMapping("/list")
	public String findByProductNameLike(Model model , @RequestParam(value = "page" , defaultValue = "0")int page ,@RequestParam(value = "productName")String productName ,@RequestParam(value = "type") String type) {
		model.addAttribute("list", this.welfareMallService.findByProductNameLike(productName, type, page, 9));
		return "welfaremallList";
	}
	@GetMapping("/create")
	public String create(WelfareMallForm welfareMallForm) {
		return "welfaremallCreate";
	}
	@PostMapping("/create")
	public String create(@Valid WelfareMallForm welfareMallForm , BindingResult bindingResult , @RequestParam("type")String type) {
		WelfareMall welfareMall = new WelfareMall();
		welfareMall.setProductName(welfareMallForm.getProductName());
		welfareMall.setDescription(welfareMallForm.getDesciption());
		welfareMall.setPrice(welfareMallForm.getPrice());
		if(welfareMallForm.getFileList()!=null&&!welfareMallForm.getFileList().isEmpty()) {
			List<Files> fileList = new ArrayList<>();		
			for(MultipartFile multipartFile : welfareMallForm.getFileList()) {
				Files file = new Files();
				file = this.fileService.uploadFile(multipartFile);
				fileList.add(file);
			}
			welfareMall.setPhotos(fileList);
		}
		welfareMall.setType(type);
		this.welfareMallService.save(welfareMall);
		return "redirect:/welfaremall/list";
	}
	@GetMapping("/detail/{id}")
	public String detailWelfaremall(Model model , @PathVariable("id")Integer id) {
		model.addAttribute("welfaremall", this.welfareMallService.findById(id));
		return "detailWelfaremall";
	}
	@GetMapping("/edit/{id}")
	public String editWelfaremall(Model model , WelfareMallForm welfareMallForm , @PathVariable("id")Integer id) {
		WelfareMall welfareMall = this.welfareMallService.findById(id);
		welfareMallForm.setProductName(welfareMall.getProductName());
		welfareMallForm.setDesciption(welfareMall.getDescription());
		welfareMallForm.setPrice(welfareMall.getPrice());
		model.addAttribute("fileList", welfareMall.getPhotos());
		return "editWelfaremall";
	}
	@PostMapping("/edit/{id}")
	public String editWelfaremall(@Valid WelfareMallForm welfareMallForm , BindingResult bindingResult ,@PathVariable("id")Integer id) {
		if(bindingResult.hasErrors()) {
			return "eidtWelfaremall";
		}
		WelfareMall welfareMall = this.welfareMallService.findById(id);
		if(!welfareMallForm.getFileList().isEmpty()) {
			List<Files> fileList = welfareMall.getPhotos();
			for(MultipartFile multipartFile : welfareMallForm.getFileList()) {
				Files file = new Files();
				file = this.fileService.uploadFile(multipartFile);
				fileList.add(file);
			}
		}
		welfareMall.setProductName(welfareMallForm.getProductName());
		welfareMall.setDescription(welfareMallForm.getDesciption());
		welfareMall.setPrice(welfareMallForm.getPrice());
		this.welfareMallService.save(welfareMall);
		return String.format("redirect:/welfaremall/detail/%s", id);
	}
	@GetMapping("/edit/{id}/{fileId}")
	public String editWelfaremallFile(@PathVariable("id")Integer id , @PathVariable("fileId")Integer fileId) {
		WelfareMall welfareMall = this.welfareMallService.findById(id);
		Files file = this.fileService.findByFiles(fileId);
		welfareMall.getPhotos().remove(file);
		this.fileService.delete(file);
		return String.format("redirect:/welfaremall/edit/%s", id);
	}
	@GetMapping("/delete/{id}")
	public String deleteWelfaremall(@PathVariable("id") Integer id) {
		WelfareMall welfareMall = this.welfareMallService.findById(id);
		List<Files> fileList = welfareMall.getPhotos();
		this.welfareMallService.delete(welfareMall);
		if(!fileList.isEmpty()) {
			for(Files file : fileList) {
				this.fileService.delete(file);
			}
		}
		return "redirect:/welfaremall/list";
	}
	@GetMapping("/addCart/{id}")
	public String addCart(@PathVariable("id")Integer id , Principal principal) {
		WelfareMall welfareMall = this.welfareMallService.findById(id);
		Users user = this.userService.getUser(principal.getName());
		Cart cart = this.cartService.findByProduct(welfareMall);
		if(cart.getProduct()!=null) {
			cart.setQuantity(cart.getQuantity()+1);
		}
		else {
			cart.setProduct(welfareMall);
			cart.setUser(user);
			cart.setPoint(welfareMall.getPrice());
			cart.setQuantity(1);
		}
		this.cartService.save(cart);
		return "redirect:/welfaremall/list";
	}
	@GetMapping("/viewCart")
	public String viewCart(Model model , Principal principal , @RequestParam(value = "type" , defaultValue = "personal") String type) {
		Users user = this.userService.getUser(principal.getName());
		model.addAttribute("cartList", this.cartService.findByUser(user, type));
		return "cartList";
	}
	@PostMapping("/viewCart")
	public String viewCart(Model model , Principal principal , @RequestParam("productName")String productName , @RequestParam(value = "type" , defaultValue = "personal")String type) {
		List<Cart> cartList = new ArrayList<>();
		for(WelfareMall welfareMall : this.welfareMallService.findByProductNameLike(productName , type)) {
			Cart cart = new Cart();
			cart = this.cartService.findByProduct(welfareMall);
			cartList.add(cart);
		}
		model.addAttribute("cartList", cartList);
		return "cartList";
	}
	@GetMapping("/deleteCart/{id}")
	public String deleteCart(@PathVariable("id")Integer id) {
		Cart cart = this.cartService.findById(id);
		if(cart.getQuantity()>1) {
			cart.setQuantity(cart.getQuantity()-1);
		}
		else {
			this.cartService.delete(cart);
		}
		return "redirect:/welfaremall/viewCart";
	}
	@GetMapping("/purchase")
	public String purchase(Model model , Principal principal , @RequestParam(value = "type" , defaultValue = "personal")String type) {
		Purchase _purchase = new Purchase();
		Users user = this.userService.getUser(principal.getName());
		List<Cart> cartList = this.cartService.findByUser(user, type);
		model.addAttribute("cartList", cartList);
		_purchase.setUser(user);
		_purchase.setDepartment(user.getPosition().getDepartment());
		if(cartList!=null&&cartList.isEmpty()) {
			List<WelfareMall> productList = new ArrayList<>();
			for(Cart cart : cartList) {
				WelfareMall welfareMall = new WelfareMall();
				welfareMall = cart.getProduct();
				productList.add(welfareMall);
			}
			_purchase.setProductList(productList);
		}
		int sum = 0;
		int quantity = 0;
		for(Cart cart : cartList) {
			quantity += cart.getQuantity();
			sum += cart.getQuantity()*cart.getProduct().getPrice();
		}
		_purchase.setQuantity(quantity);
		_purchase.setTotalPrice(sum);
		_purchase.setPurchaseStatus("queue");
		_purchase.setPurchaseType(type);
		Purchase purchase = this.purchaseService.save(_purchase);
		model.addAttribute("purchase", purchase);
		return "purchase";
	}
	@PostMapping("/purchase")
	public String purchase(Principal principal , @RequestParam(value = "type")String type) {
		Users user = this.userService.getUser(principal.getName());
		Purchase purchase = this.purchaseService.findByUserAndPurchaseType(user, type);
		for(Cart cart : this.cartService.findByUser(user, type)) {
			this.cartService.delete(cart);
		}
		purchase.setPurchaseStatus("process");
		this.purchaseService.save(purchase);
		return "redirect:/welfaremall/viewCart";
	}
}
