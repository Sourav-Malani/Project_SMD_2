<?php
include 'db_config.php'; // Include the database configuration

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $fullName = $_POST['fullName'];
    $email = $_POST['email'];
    $password = $_POST['password'];
    $deliveryAddress = $_POST['deliveryAddress'];
    $phoneNo = $_POST['phoneNo'];
    $imageURL = $_POST['imageURL'];

    // Create a database connection
    $conn = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    // Check if the connection was successful
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    // Insert user data into the database
    $sql = "INSERT INTO users (user_name, user_email, user_password, user_address, user_phone, user_image_url) VALUES ('$fullName', '$email', '$password', '$deliveryAddress', '$phoneNo', '$imageURL')";

    if ($conn->query($sql) === TRUE) {
        echo "SignUp Successful";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }

    $conn->close();
} else {
    echo "Invalid request method";
}
?>
