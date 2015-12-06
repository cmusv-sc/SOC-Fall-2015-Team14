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
})
