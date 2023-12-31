<?php
include 'db_connect.php'; // Make sure you have this file for database connection

// Check if the request is a POST request
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Get data from POST request
    $email = $_POST['email'];
    $full_name = $_POST['full_name'];
    $delivery_address = $_POST['delivery_address'];
    $phone_no = $_POST['phone_no'];

    // Check if email exists in the database
    $checkEmail = $conn->prepare("SELECT email FROM users WHERE email = ?");
    $checkEmail->bind_param("s", $email);
    $checkEmail->execute();
    $checkEmailResult = $checkEmail->get_result();

    if ($checkEmailResult->num_rows > 0) {
        // Update user data
        $updateQuery = $conn->prepare("UPDATE users SET full_name=?, delivery_address=?, phone_no=? WHERE email=?");
        $updateQuery->bind_param("ssss", $full_name, $delivery_address, $phone_no, $email);

        if ($updateQuery->execute()) {
            //echo "User data updated successfully";
            echo json_encode(array("status" => "success", "message" => "User data updated successfully"));
        } else {
            echo json_encode(array("status" => "error", "message" => "Error updating user data". $updateQuery->error));

            //echo "Error updating user data: " . $updateQuery->error;
        }

        $updateQuery->close();
    } else {
        //echo "User not found with the provided email";
        echo json_encode(array("status" => "error", "message" => "User not found with the provided email"));

    }

    $checkEmail->close();
}
$conn->close();
?>
