$(document).ready(function(){
    $('#autorizadastodos').on('click',function(){
        if(this.checked){
            $('.checkbox').each(function(){
                this.checked = true;
                $('.autorizada').prop("value" , true)
            });
        }else{
             $('.checkbox').each(function(){
                this.checked = false;
                $('.autorizada').prop("value" , false)
            });
        }
    });
    
    $('.checkbox').on('click',function(){
    	
    	var name = $(this).closest('td').find('.autorizada').prop("value" , this.checked );
    	
        if($('.checkbox:checked').length == $('.checkbox').length){
            $('#autorizadastodos').prop('checked',true);
        }else{
            $('#autorizadastodos').prop('checked',false);
        }
    });
});