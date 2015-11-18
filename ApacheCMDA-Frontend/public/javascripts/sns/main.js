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
}

function sub(obj){
    var file = obj.value;
    var fileName = file.split("\\");
    document.getElementById("yourBtn").innerHTML = fileName[fileName.length-1];
    document.myForm.submit();
    event.preventDefault();
}