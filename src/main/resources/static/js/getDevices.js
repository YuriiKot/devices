let d = new Date();
document.body.innerHTML += "<p>Today's date is " + d + "</p>"


function getUser(userId, callback) {
    var usersUrl = "http://www.serveraddress.com/api/users/";
    var params = userId;
    var xhttp = new XMLHttpRequest();

    xhttp.addEventListener('load', callback);
    xhttp.addEventListener('error', () => console.log("Request to "+usersUrl+params+" failed"));

    xhttp.open("GET", usersUrl + params, true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send();
}
getUser(1, function() {
    console.log(JSON.parse(this.responseText));
});