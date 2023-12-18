<?php
include 'db_connect.php'; // Make sure you have this file for database connection

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $email = $_POST['email'];

    // Check if the user exists in the database
    $stmt = $conn->prepare("SELECT * FROM users WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();
    $user = $result->fetch_assoc();
    
    // Create an array to store the response data
    $response = array();

    if ($user) {
        // Check if "phone" is set in the user data, if not, set it to null
        $phone = isset($user['phone_no']) ? $user['phone_no'] : null;

        // Add user data to the response array
        $response = array(
            "status" => "success",
            "full_name" => $user['full_name'],
            "email" => $user['email'],
            "phone_no" => $phone,
            "delivery_address" => $user['delivery_address'], 
            "image_url"=> $user['image_url'] // Use the $phone variable here
            // Add any other user data you need to send
        );
    } else {
        $response = array("status" => "error", "message" => "User does not exist");
    }

    // Encode the response array as JSON
    echo json_encode($response);

    $stmt->close();
}
$conn->close();

?>
