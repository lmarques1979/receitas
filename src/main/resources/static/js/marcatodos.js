$(function () {

            var chkId = '';
            $('.chklinha:checked').each(function () {
                chkId += $(this).val() + ",";
            });
            chkId = chkId.slice(0, -1);

           $('.marcartodos').change(function () {
                $('.chklinha').prop('checked', this.checked);
            });

           $('.chklinha').change(function() { $('.todos').prop('checked',$('.chklinha:checked').length == $('.chklinha').length);
     });
});