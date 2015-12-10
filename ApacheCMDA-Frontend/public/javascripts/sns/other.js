/**
 * Created by lixunrong on 12/4/15.
 */
$(document).ready(function() {

    if($("#hiddenFollowFlag").length > 0) {
        $(".glyphicon.glyphicon-plus.follow-btn").hide();
        $(".glyphicon.glyphicon-ok.unfollow-btn").show();
    } else {
        $(".glyphicon.glyphicon-plus.follow-btn").show();
        $(".glyphicon.glyphicon-ok.unfollow-btn").hide();
    }

    $(".glyphicon.glyphicon-plus.follow-btn").click(function() {

        var id = $("#hiddenSession").val();
        var that = $(this);
        $.ajax({
            url: "/sns/users/follow/" + id,
            type: "GET"
        }).done(function() {
            $(that).hide();
            $(".glyphicon.glyphicon-ok.unfollow-btn").show();
        });
    })

    $(".glyphicon.glyphicon-ok.unfollow-btn").click(function() {

        var id = $("#hiddenSession").val();
        var that = $(this);
        $.ajax({
            url: "/sns/users/unfollow/" + id,
            type: "GET"
        }).done(function() {
            $(that).hide();
            $(".glyphicon.glyphicon-plus.follow-btn").show();
        });
    })
})
