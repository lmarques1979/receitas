tinymce.init({
		    selector: "textarea",
		    plugins: [
		        "fullpage advlist autolink lists link image charmap print preview anchor",
		        "searchreplace visualblocks code",
		        "insertdatetime media table contextmenu paste"
		    ],
		    height:270,
		    fullpage_default_fontsize: "12px",
		    fontsize_formats: "10pt 12pt 14pt",
		    menubar: false,
	        toolbar_items_size: 'small',
		    toolbar1: "insertfile undo redo | styleselect | bold italic| alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image",
		    toolbar2: "styleselect fontselect fontsizeselect"
});