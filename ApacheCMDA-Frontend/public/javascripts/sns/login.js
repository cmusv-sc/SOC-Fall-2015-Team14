/**
 * Created by lixunrong on 11/17/15.
 */

$(document).ready(function() {
    function checkpwd(){
        var p1=document.getElementById("password").value;
        var p2=document.getElementById("repassword").value;
        if(p1!=p2){
            document.getElementById("msg").innerHTML="Please check that your passwords match";
            document.getElementById("create").disabled = true;

        }else{
            document.getElementById("msg").innerHTML = "";
            document.getElementById("create").disabled = false;
            return true;
        }
    }

    function isEmailExisted() {
        var email = document.getElementById("email").value;
        var obj = {
            email : email
        }
        $.ajax({
            url: "/isEmailExisted",
            data: JSON.stringify(obj),
            headers: {
                'Content-Type': 'application/json'
            },
            type: "POST"
        }).done(function(data) {
            console.log(data);
            var response = data;
            if("error" in response){
                document.getElementById("msg1").innerHTML = "Email already used";
                document.getElementById("create").disabled = true;
            }else{
                document.getElementById("msg1").innerHTML = "";
                document.getElementById("create").disabled = false;
                return true;
            }
        });
    }
})
