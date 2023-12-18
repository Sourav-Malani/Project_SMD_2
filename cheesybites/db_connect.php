<?php
$servername = "localhost";
$username = "root"; // default username for localhost
$password = ""; // default password for localhost
$dbname = "cheesybites"; // replace with your database name
$apiBaseUrl = "http://192.168.18.114/cheesybites/";
// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>
