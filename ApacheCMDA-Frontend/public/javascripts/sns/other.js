/**
 * Created by lixunrong on 12/4/15.
 */
$(document).ready(function() {
    $(".glyphicon.glyphicon-plus.follow-btn").click(function() {
        console.log("click");
        var id = $("#hiddenSession").val();
        var that = $(this);
        $.ajax({
            url: "/sns/users/follow/" + id,
            type: "GET"
        }).done(function() {
            $(that).removeClass("glyphicon-plus");
            $(that).addClass("glyphicon-ok");
        });
    })
})
