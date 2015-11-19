/**
 * Created by lixunrong on 11/17/15.
 */


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
            url: "/sns/isEmailExisted",
            data: JSON.stringify(obj),
            headers: {
                'Content-Type': 'application/json'
            },
            type: "POST"
        }).done(function(data) {
            console.log(data);
            document.getElementById("msg1").innerHTML = "Email already used";
            document.getElementById("create").disabled = true;
            return true;

        }).error(function(error) {
            console.log(error)
            document.getElementById("msg1").innerHTML = "";
            document.getElementById("create").disabled = false;
            return true;
        });
    }

    function isUserNameExisted() {
        var userName = $("#userName").val();

        if (userName == "" || userName == null) {
            document.getElementById("create").disabled = false;
            document.getElementById("msg0").innerHTML = "";
            return true;
        }

        var obj = {
            userName : userName
        }

        $.ajax({
            url: "/sns/isUserNameExisted",
            data: JSON.stringify(obj),
            headers: {
                'Content-Type': 'application/json'
            },
            type: "POST"
        }).done(function(response) {
            console.log(response);
            if ("error" in response) {
                document.getElementById("msg0").innerHTML = "UserName already used";
                document.getElementById("create").disabled = true;
            } else {
                document.getElementById("msg0").innerHTML = "";
                document.getElementById("create").disabled = false;
                return true;
            }
        })
    }

