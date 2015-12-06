/**
 * Created by lixunrong on 11/17/15.
 */
$(document).ready(function() {

    //store the session
    var userName = $("input#hiddenUserName").val();
    var userId = $("input#hiddenUserId").val();

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
            userName : $("#profile-form .profile-username input").val(),
            firstName: $("#profile-form .profile-name input[name='firstName']").val(),
            lastName: $("#profile-form .profile-name input[name='lastName']").val(),
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

    //click comment to slide down
    $(".comment-btn").click(function() {
        var post = $(this).closest(".post");
        var comment = $(post).find(".post-footer");

        if ($(comment).is(':hidden')) {
            $(comment).slideDown(400);
        } else {
            $(comment).slideUp(400);
        }
    })


    //submit comment
    $(".submit-comment").click(function() {
        var comment = $(this).closest(".post-footer");
        var id = $(this).attr('id').split('-')[1];
        var content = $(this).parent().prev("input.form-control");
        var text = content.val();
        console.log(id + content);
        var obj = {
            content: text,
            postId: id
        }

        //send ajax request to create new comment
        $.ajax({
            url: "/sns/posts/newComment",
            type: "POST",
            data: JSON.stringify(obj),
            headers: {
                'Content-Type': 'application/json'
            }
        }).done(function(data) {

            content.val("");
            //display the comment on the screen
            var list = $(comment).find("ul.comments-list");
            var dt = new Date();
            var time = dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();

            $(list).prependTo(
                '<li class="comment"> <a class="pull-left" href="#">' +
                '<img class="avatar" src="http://localhost:9034/users/getPhoto/' + userId +'" alt="avatar"> </a>' +
                '<div class="comment-body"> <div class="comment-heading"> <h4 class="user">'+ userName + '</h4>' +
                '<h5 class="time">' + time + '</h5> </div>' +
                '<p>' + text + '</p> </div> </li>'
            )

        }).error(function(error) {
            console.log(error);
        })
    })


    //click like
    $(".like-btn").click(function() {
        var post = $(this).closest(".post");
        var id = $(post).attr('id').split('-')[1];
        var likeCount = $(this).text();
        likeCount++;

        var that = $(this);

        $.ajax({
            url: "/sns/posts/newLike",
            type: "POST",
            data: JSON.stringify({
                postId: id
            }),
            headers: {
                'Content-Type': 'application/json'
            }
        }).done(function(data) {

            $(that).html(
                '<i class="fa fa-thumbs-up icon"></i>' + likeCount
            );

        }).error(function(error) {
            console.log(error);
        })
    })

    //click share
    $(".share-btn").click(function() {
        var post = $(this).closest(".post");
        var id = $(post).attr('id').split('-')[1];
        var shareCount = $(this).text();
        shareCount++;
        var that = $(this);

        $.ajax({
            url: "/sns/posts/newShare",
            type: "POST",
            data: JSON.stringify({
                postId: id
            }),
            headers: {
                'Content-Type': 'application/json'
            }
        }).done(function(data) {
            $(that).html(
                '<i class="fa fa-share icon"></i>' + shareCount
            );

        }).error(function(error) {
            console.log(error);
        })
    })

    //click more btn
    $(".more-btn").click(function() {
        $(this).hide();
        var posts = $(".container.bootstrap.snippet");
        posts.each(function() {
            if ($(this).is(':hidden')) {
                $(this).show();
            }
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

