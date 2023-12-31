<?php
include 'db_connect.php'; // Make sure you have this file for database connection

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $email = $_POST['email'];
    $password = $_POST['password'];

    // Check if the user exists in the database
    $stmt = $conn->prepare("SELECT * FROM users WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();
    $user = $result->fetch_assoc();

    if ($user) {
        // Verify the password (use password_verify if you're hashing passwords)
        if ($user['pass'] === $password) {
            // Set the 'logged_in' status to 'true' for the user
            $updateStmt = $conn->prepare("UPDATE users SET logged_in = 1 WHERE email = ?");
            $updateStmt->bind_param("s", $email);
            $updateResult = $updateStmt->execute();

            if ($updateResult) {
                echo json_encode(array(
                    "status" => "success",
                    "name" => $user['full_name'],
                    "email" => $user['email'],
                    "pass" => $user['pass'],
                    "delivery_address" => $user['delivery_address'],
                    "phone" => $user['phone_no'],
                    "image_url" => $user['image_url'],
                    "logged_in" => true // Indicate that the user is now logged in
                ));
            } else {
                echo json_encode(array("status" => "error", "message" => "Failed to update 'logged_in' status"));
            }
        } else {
            echo json_encode(array("status" => "error", "message" => "Invalid Credentials"));
        }
    } else {
        echo json_encode(array("status" => "error", "message" => "User does not exist"));
    }

    $stmt->close();
}
$conn->close();

?>
