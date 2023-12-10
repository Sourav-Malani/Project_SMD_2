<?php
include 'db_config.php'; // Include the database configuration

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $email = $_POST['email'];
    $password = $_POST['password'];

    // Create a database connection
    $conn = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    // Check if the connection was successful
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    // Retrieve user data from the database
    $sql = "SELECT * FROM users WHERE user_email = '$email' AND user_password = '$password'";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        // User found, send back user data
        $row = $result->fetch_assoc();
        echo json_encode($row);
    } else {
        // User not found
        echo "User not found";
    }

    $conn->close();
} else {
    echo "Invalid request method";
}
?>
