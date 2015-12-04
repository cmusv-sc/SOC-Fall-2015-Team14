/**
 * Created by lixunrong on 12/4/15.
 */
$(document).ready(function() {
    $(".glyphicon.glyphicon-plus").click(function() {
        var id = $("#hiddenSession").val();
        $.ajax({
            url: "/sns/users/follow/" + id,
            type: "GET"
        }).done(function() {
            $(this).removeClass("glyphicon-plus");
            $(this).addClass("glyphicon-ok");
        });
    })
})
