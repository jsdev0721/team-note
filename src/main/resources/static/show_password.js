$(function(){
  // 눈표시 클릭 시 패스워드 보이기
  $('.eyes').on('click',function(){
    $('.input.password').toggleClass('active');

    if( $('.input.password').hasClass('active') == true ){
    	$(this).find('.bi-eye-slash').attr('class',"bi bi-eye").parents('.input').find('#password').attr('type',"text");
    				// i 클래스                // 텍스트 보이기 i 클래스
    }
    else{
    	$(this).find('.bi-eye').attr('class',"bi bi-eye-slash").parents('.input').find('#password').attr('type','password');
    }
  });
  
  $('.eyesCheck').on('click',function(){
    $('.input.passwordCheck').toggleClass('active');

    if( $('.input.passwordCheck').hasClass('active') == true ){
    	$(this).find('.bi-eye-slash').attr('class',"bi bi-eye").parents('.input').find('#passwordCheck').attr('type',"text");
    				// i 클래스                // 텍스트 보이기 i 클래스
    }
    else{
    	$(this).find('.bi-eye').attr('class',"bi bi-eye-slash").parents('.input').find('#passwordCheck').attr('type','password');
    }
  });
});
