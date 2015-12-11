/**
 * Created by xiaofenl on 12/4/15.
 */
$(document).ready(function() {
    var x = document.getElementById("loc");
    var coord;
    $("#location").click(function() {
        console.log("click");
        $( "div.btnTitle" ).replaceWith("<span class=\"glyphicon glyphicon-refresh spinning\" id=\"btnTitle\"></span>");
        if (navigator.geolocation) {
             navigator.geolocation.getCurrentPosition(showCity);
        } else {
             x.innerHTML = "Geolocation is not supported by this browser.";
        }

    })

    function showCity(position) {
        var geocoder;
        geocoder = new google.maps.Geocoder();
        var latlng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);

        geocoder.geocode(
            {'latLng': latlng},
            function(results, status) {
                if (status == google.maps.GeocoderStatus.OK) {
                        if (results[0]) {
                            var add= results[0].formatted_address ;
                            var  value=add.split(",");

                            count=value.length;
                            country=value[count-1];
                            state=value[count-2];
                            city=value[count-3];
                            $("#btnTitle").replaceWith( "<h5>" + city + "</h5>" );

                            $("#curlocation").val(city);
                            $('#btnTitle').attr("disabled", true);
                        }
                        else  {
//                            alert("address not found");
                        }
                }
                 else {
                    alert("Geocoder failed due to: " + status);
                }
            }
        );

    }


})
