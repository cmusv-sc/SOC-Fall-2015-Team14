/**
 * Created by lixunrong on 12/4/15.
 */
$(document).ready(function() {

    //hover to get delete button
    $(".container.bootstrap.snippet").hover(function () {

        //add delete button
        var removeBtn = $(this).find(".pull-right.meta.remove-div");
        if ($(removeBtn).is(":hidden")) {
            $(removeBtn).show();
        }

        var editBtn = $(this).find(".pull-right.meta.edit-div");
        if ($(editBtn).is(":hidden")) {
            $(editBtn).show();
        }

    }, function () {
        var removeBtn = $(this).find(".pull-right.meta.remove-div");
        if (!$(removeBtn).is(":hidden")) {
            $(removeBtn).hide();
        }

        var editBtn = $(this).find(".pull-right.meta.edit-div");
        if (!$(editBtn).is(":hidden")) {
            $(editBtn).hide();
        }
    })

    //click delete button
    $(".remove-btn").click(function() {

        var post = $(this).closest(".panel.panel-white.post.panel-shadow");
        var postId = $(post).attr('id').split('-')[1];
        $.ajax({
            url: "/sns/posts/" + postId + "/delete/",
            type: "GET"
        })

        $(post).remove();
    })


    //click edit button
    $(".edit-btn").click(function() {
        var post = $(this).closest(".panel.panel-white.post.panel-shadow");
        var postId = $(post).attr('id').split('-')[1];
    })

    editProfile();


    //support france
    $("#support-france-btn").click(function() {
        $.ajax({
            url: "/sns/addFrontLayer",
            type: "GET"
        })

        //set front end
        $(".profile-container").append('<div class="profile-image img-rounded front-layer-image"></div>');

        $(".panel.panel-white.post.panel-shadow .post-heading .pull-left.image a").append('<div class="img-circle avatar front-layer-image"></div>');
    })

    //remvoe layer
    $("#remove-layer-btn").click(function(){
        $.ajax({
            url: "/sns/removeFrontLayer",
            type: "GET"
        })

        //set front end
        $(".profile-container .profile-image.img-rounded.front-layer-image").remove();

        $(".panel.panel-white.post.panel-shadow .post-heading .pull-left.image a .img-circle.avatar.front-layer-image").remove();
    })
})


function editProfile() {
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
}
