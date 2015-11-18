/**
 * Created by lixunrong on 11/17/15.
 */
$(document).ready(function() {
    //edit profile
    $("#edit_btn").on("click", function() {
        $(".profile-container").hide();
        $(".profile-edit-container").show();
    });



})


function getFile(){
    $("#upfile").click();
    e.preventDefault();
}

function sub(obj){
    if (obj.files && obj.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $(".profile-image").css('background-image', 'url(' + e.target.result + ')');
        }

        reader.readAsDataURL(obj.files[0]);
    }
}