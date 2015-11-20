/**
 * Created by lixunrong on 11/17/15.
 */
$(document).ready(function() {
    //edit profile
    $("#edit_btn").on("click", function() {
        $(".profile-container").hide();
        $(".profile-edit-container").show();
    });

    $("#profile-form").submit(function(e) {
        e.preventDefault();
    })


    $("#save-profile").click(function() {
        var obj = {
            userName : $("#profile-form .profile-name input").val(),
            affiliation : $("#profile-form .profile-affiliation input").val(),
            email : $("#profile-form .profile-email input").val(),
            researchInterests : $("#profile-form .profile-interest input").val(),
            phoneNumber : $("#profile-form .profile-phone input").val()
        }

        $.ajax({
            url: "/sns/users/updateUser",
            type: "POST",
            data: JSON.stringify(obj),
            headers: {
                'Content-Type': 'application/json'
            }
        }).done(function(data) {
            console.log(data);
            window.location.reload();
        }).error(function(error) {
            console.log(error);
        })
    })
})


function getFile(){
    $("#upfile").click();
    //e.preventDefault();
}

function sub(obj){
    if (obj.files && obj.files[0]) {
        var reader = new FileReader();

        console.log("ready to submit");

        var image = obj.files[0];
        var userId = $("#hiddenSession").val();
        var formData = new FormData($('#profile-image-form')[0]);

        console.log("ajax");
        $.ajax({
            url: "http://localhost:9034/users/uploadPhoto/" + userId,
            type: "POST",
            //xhr: function() {  // Custom XMLHttpRequest
            //    var myXhr = $.ajaxSettings.xhr();
            //    if(myXhr.upload){ // Check if upload property exists
            //        //myXhr.upload.addEventListener('progress',progressHandlingFunction, false); // For handling the progress of the upload
            //    }
            //    return myXhr;
            //},
            data: formData,
            //Options to tell jQuery not to process data or worry about content-type.
            cache: false,
            contentType: false,
            processData: false
        }).done(function(data) {
            console.log(data);
        }).error(function(err) {
            console.log(err);
        });


        reader.onload = function (e) {
            $(".profile-image").css('background-image', 'url(' + e.target.result + ')');
        }

        reader.readAsDataURL(obj.files[0]);
    }
}

