package com.groupware.note.welfaremall;

import java.net.MalformedURLException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
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
import com.groupware.note.user.UserDetails;
import com.groupware.note.user.UserDetailsService;
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
	private final UserDetailsService userDetailsService;
	
	public boolean checkProduct(List<Cart> cartList , WelfareMall welfareMall) {
		for(Cart cart : cartList) {
			if(cart.getProduct()==welfareMall) {
				cart.setQuantity(cart.getQuantity()+1);
				this.cartService.save(cart);
				return true;
			}
		}
		return false;
	}
	
	@GetMapping("/list")
	public String findAll(Model model , @RequestParam(value = "page" , defaultValue = "0") int page ,@RequestParam(defaultValue = "personal" , value = "type")String type , Principal principal) {
		model.addAttribute("list", this.welfareMallService.findAll(type , page, 9));
		Users user = this.userService.getUser(principal.getName());
		model.addAttribute("cartList", this.cartService.findByUser(user, type));
		model.addAttribute("type", type);
		return "welfaremall/welfaremallList";
	}
	
	@PostMapping("/list")
	public String findByProductNameLike(Model model , @RequestParam(value = "page" , defaultValue = "0")int page ,@RequestParam(value = "productName")String productName ,@RequestParam(value = "type") String type) {
		model.addAttribute("list", this.welfareMallService.findByProductNameLike(productName, type, page, 9));
		model.addAttribute("type", type);
		return "welfaremall/welfaremallList";
	}
	@GetMapping("/create")
	public String create(WelfareMallForm welfareMallForm) {
		return "welfaremall/welfaremallCreate";
	}
	@PostMapping("/create")
	public String create(@Valid WelfareMallForm welfareMallForm , BindingResult bindingResult , @RequestParam("type")String type) {
		if(bindingResult.hasErrors()) {
			return "welfaremall/welfaremallCreate";
		}
		WelfareMall welfareMall = new WelfareMall();
		welfareMall.setProductName(welfareMallForm.getProductName());
		welfareMall.setDescription(welfareMallForm.getDesciption());
		welfareMall.setPrice(welfareMallForm.getPrice());
		if(welfareMallForm.getMainImage()!=null&&!welfareMallForm.getMainImage().isEmpty()) {
			Files file = this.fileService.uploadFile(welfareMallForm.getMainImage());
			welfareMall.setMainImage(file);
		}
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
		WelfareMall _welfareMall = this.welfareMallService.save(welfareMall);
		return "redirect:/welfaremall/list";
	}
	@GetMapping("/detail/{id}")
	public String detailWelfaremall(Model model , @PathVariable("id")Integer id , Principal principal) {
		if(principal!=null) {
			model.addAttribute("userInfo", this.userService.getUser(principal.getName()));
		}
		else {
			model.addAttribute("userInfo", null);
		}
		WelfareMall welfareMall = this.welfareMallService.findById(id);
		model.addAttribute("welfaremall", welfareMall);
		return "welfaremall/welfaremallDetail";
	}
	@GetMapping("/photo/{id}")
	public ResponseEntity<Resource>viewPhoto(@PathVariable("id")Integer id) throws MalformedURLException{
		Files file = this.fileService.findByFiles(id);
		return this.fileService.photoView(file);
	}
	@GetMapping("/edit/{id}")
	public String editWelfaremall(Model model , WelfareMallForm welfareMallForm , @PathVariable("id")Integer id) {
		WelfareMall welfareMall = this.welfareMallService.findById(id);
		welfareMallForm.setProductName(welfareMall.getProductName());
		welfareMallForm.setDesciption(welfareMall.getDescription());
		welfareMallForm.setPrice(welfareMall.getPrice());
		model.addAttribute("welfaremall", welfareMall);
		model.addAttribute("fileList", welfareMall.getPhotos());
		return "welfaremall/welfaremallEdit";
	}
	@PostMapping("/edit/{id}")
	public String editWelfaremall(@Valid WelfareMallForm welfareMallForm , BindingResult bindingResult ,@PathVariable("id")Integer id) {
		if(bindingResult.hasErrors()) {
			return "welfaremall/welfaremallDetail";
		}
		WelfareMall welfareMall = this.welfareMallService.findById(id);
		if(welfareMallForm.getMainImage()!=null&&!welfareMallForm.getMainImage().isEmpty()) {
			Files file = welfareMall.getMainImage()!=null ? this.fileService.updateFile(welfareMall.getMainImage(), welfareMallForm.getMainImage()) : this.fileService.uploadFile(welfareMallForm.getMainImage());
			welfareMall.setMainImage(file);
		}
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
	public String addCart(@PathVariable("id")Integer id , Principal principal , @RequestParam("to") String to , @RequestParam("type")String type) {
		WelfareMall welfareMall = this.welfareMallService.findById(id);
		Users user = this.userService.getUser(principal.getName());
		Cart cart = this.cartService.findByProductAndUser(welfareMall, user);
		if(cart.getQuantity()!=null) {
			cart.setQuantity(cart.getQuantity()+1);
		}
		else {
			cart.setType(welfareMall.getType());
			cart.setProduct(welfareMall);
			cart.setUser(user);
			cart.setPoint(welfareMall.getPrice());
			cart.setQuantity(1);
		}
		this.cartService.save(cart);
		if(to.equals("list")) {
			return String.format("redirect:/welfaremall/list?type=%s", type);			
		}
		return String.format("redirect:/welfaremall/viewCart?type=%s", type);
	}
	@GetMapping("/deleteCart/{id}")
	public String deleteCart(@PathVariable("id")Integer id,@RequestParam("to") String to , Principal principal , @RequestParam("type")String type) {
		System.out.println(type);
		Users user = this.userService.getUser(principal.getName());
		WelfareMall welfareMall = this.welfareMallService.findById(id);
		Cart cart = this.cartService.findByProductAndUser(welfareMall, user);
		if(cart.getQuantity()>1) {
			cart.setQuantity(cart.getQuantity()-1);
			this.cartService.save(cart);
		}
		else {
			this.cartService.delete(cart);
		}
		if(to.equals("list")) {
			return String.format("redirect:/welfaremall/list?type=%s", type);
		}
		return String.format("redirect:/welfaremall/viewCart?type=%s", type);
	}
	@GetMapping("/viewCart")
	public String viewCart(Model model , Principal principal , @RequestParam(value = "type" , defaultValue = "personal") String type) {
		Users user = this.userService.getUser(principal.getName());
		model.addAttribute("cartList", this.cartService.findByUser(user, type));
		model.addAttribute("type", type);
		return "welfaremall/cartList";
	}
	@PostMapping("/viewCart")
	public String viewCart(Model model , Principal principal , @RequestParam("productName")String productName , @RequestParam(value = "type" , defaultValue = "personal")String type) {
		List<Cart> cartList = new ArrayList<>();
		Users user = this.userService.getUser(principal.getName());
		for(WelfareMall welfareMall : this.welfareMallService.findByProductNameLike(productName , type)) {
			Cart cart = new Cart();
			cart = this.cartService.findByProductAndUser(welfareMall, user);
			if(cart.getCartId()!=null) {
				cartList.add(cart);	
			}
		}
		model.addAttribute("run", true);
		model.addAttribute("cartList", cartList);
		return "welfaremall/cartList";
	}
	@GetMapping("/purchase")
	public String purchase(Model model , Principal principal , @RequestParam(value = "type" , defaultValue = "personal")String type) {
		Users user = this.userService.getUser(principal.getName());
		List<Purchase> purchaseList = this.purchaseService.findByUserAndPurchaseTypeAndPurchaseStatus(user, type , "queue");
		List<Cart> cartList = this.cartService.findByUser(user, type);
		model.addAttribute("cartList", cartList);
		Purchase _purchase = purchaseList.isEmpty() ? new Purchase() : purchaseList.get(0);
		if(cartList!=null&&!cartList.isEmpty()) {
			List<WelfareMall> productList = new ArrayList<>();
			for(Cart cart : cartList) {
				WelfareMall welfareMall = new WelfareMall();
				welfareMall = cart.getProduct();
				productList.add(welfareMall);
			}
			for(Purchase purchase :purchaseList) {
				if(purchase.getProductList()==productList) {
					model.addAttribute("purchase", purchase);
					model.addAttribute("type", type);
					UserDetails userDetail = this.userDetailsService.findByUser(user);
					boolean run = false;
					if(type.equals("personal")) {
						run = userDetail.getPoints()>=purchase.getTotalPrice() ? true : false;
					}else {
						run = user.getPosition().getDepartment().getPoints()>=purchase.getTotalPrice() ? true : false;
					}
					model.addAttribute("run", run);
					return "welfaremall/purchase";
				}
				
			}
			_purchase.setProductList(productList);
		}
		_purchase.setUser(user);
		_purchase.setDepartment(user.getPosition().getDepartment());
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
		model.addAttribute("type", type);
		UserDetails userDetail = this.userDetailsService.findByUser(user);
		boolean run = false;
		if(type.equals("personal")) {
			run = userDetail.getPoints()>=purchase.getTotalPrice() ? true : false;
		}else {
			run = user.getPosition().getDepartment().getPoints()>=purchase.getTotalPrice() ? true : false;
		}
		model.addAttribute("run", run);
		return "welfaremall/purchase";
	}
	@PostMapping("/purchase/{id}")
	public String purchase(@PathVariable("id")Integer id, Principal principal , @RequestParam(value = "type")String type) {
		Users user = this.userService.getUser(principal.getName());
		Purchase purchase = this.purchaseService.findById(id);
		UserDetails userDetail = this.userDetailsService.findByUser(user);
		List<Cart> cartList = this.cartService.findByUser(user, type);
		if(userDetail.getPoints()>=purchase.getTotalPrice()&&type.equals("personal")) {
			for(Cart cart : cartList) {
				this.cartService.delete(cart);
			}
			long calc = user.getPosition().getDepartment().getPoints()-purchase.getTotalPrice();
			userDetail.setPoints(calc);
			purchase.setPurchaseStatus("process");
			this.purchaseService.save(purchase);			
		}else if(user.getPosition().getDepartment().getPoints()>purchase.getTotalPrice()&&type.equals("group")) {
			for(Cart cart : cartList) {
				this.cartService.delete(cart);
			}
			long calc = user.getPosition().getDepartment().getPoints()-purchase.getTotalPrice();
			user.getPosition().getDepartment().setPoints(calc);
			purchase.setPurchaseStatus("process");
			this.purchaseService.save(purchase);
		}
		return "redirect:/welfaremall/viewCart";
	}
	@GetMapping("/purchaseRecord")
	public String purchaseRecord(Model model , Principal principal ,@RequestParam(value = "type" , defaultValue = "personal") String type) {
		Users user = this.userService.getUser(principal.getName());
		List<Purchase> purchase = this.purchaseService.findByUserAndPurchaseType(user, type);
		model.addAttribute("purchase", purchase);
		model.addAttribute("type", type);
		return "welfaremall/purchaseRecord";
	}
	@GetMapping("/cancelPurchase/{id}")
	public String cancelPurchase(@PathVariable("id")Integer id , Principal principal) {
		Users user = this.userService.getUser(principal.getName());
		Purchase purchase = this.purchaseService.findById(id);
		if(purchase.getPurchaseStatus().equals("complete")) {
			return "redirect:/welfaremall/list";
		}
		List<WelfareMall> productList = purchase.getProductList();
		List<Cart> cartList = this.cartService.findByUser(user, purchase.getPurchaseType());
		for(WelfareMall welfareMall : productList) {
			if(!checkProduct(cartList, welfareMall)) {
				Cart cart = new Cart();
				cart.setType(welfareMall.getType());
				cart.setProduct(welfareMall);
				cart.setUser(user);
				cart.setPoint(welfareMall.getPrice());
				cart.setQuantity(1);
				this.cartService.save(cart);
			}
		}
		if(purchase.getPurchaseType().equals("personal")) {
			UserDetails userDetail = this.userDetailsService.findByUser(user);
			long calc = userDetail.getPoints()+purchase.getTotalPrice();
			userDetail.setPoints(calc);
		}
		else {
			long calc = user.getPosition().getDepartment().getPoints()+purchase.getTotalPrice();
			user.getPosition().getDepartment().setPoints(calc);
		}
		this.purchaseService.delete(purchase);
		return "redirect:/welfaremall/list";
	}

}
