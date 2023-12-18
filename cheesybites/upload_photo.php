<?php

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");

include 'db_connect.php';

// Initialize an error message variable
$errorMSG = null;
$upload_path = __DIR__ . '/upload/'; // set upload folder path

if(isset($_FILES['sendimage']) && isset($_POST['userEmail'])) {
    $fileName  = $_FILES['sendimage']['name'];
    $tempPath  = $_FILES['sendimage']['tmp_name'];
    $fileSize  = $_FILES['sendimage']['size'];
    $userEmail = $_POST['userEmail'];
} else {
    $errorMSG = json_encode(array("message" => "sendimage, userEmail, ", "status" => false));
    echo $errorMSG;
    exit; // Exit the script if any parameter is missing
}

if(empty($fileName)) {
    $errorMSG = json_encode(array("message" => "Please select an image", "status" => false));
    echo $errorMSG;
} else {
    // valid image extensions
    $valid_extensions = array('jpeg', 'jpg', 'png', 'gif');

    // allow valid image file formats
    if(in_array(strtolower(pathinfo($fileName, PATHINFO_EXTENSION)), $valid_extensions)) {
        // Create unique file name
        $newFileName = uniqid() . '.' . strtolower(pathinfo($fileName, PATHINFO_EXTENSION));

        // Check file size '5MB'
        if (!file_exists($upload_path)) {
            mkdir($upload_path, 0755, true); // Create directory if it doesn't exist
        }
    
        $newFilePath = $upload_path . $newFileName;
        if (move_uploaded_file($tempPath, $newFilePath)) {
            $imageUrl = $apiBaseUrl . 'upload/' . $newFileName;
            

            // Update database
            $stmt = $conn->prepare("UPDATE users SET image_url = ? WHERE email = ?");
            $stmt->bind_param("ss", $imageUrl, $userEmail);
            $stmt->execute();
        
        } else {
            $errorMSG = json_encode(array("message" => "Failed to move uploaded file.", "status" => false));
            echo $errorMSG;
        }
    } else {
        $errorMSG = json_encode(array("message" => "Sorry, only JPG, JPEG, PNG & GIF files are allowed", "status" => false));
        echo $errorMSG;
    }
}

// if no error caused, continue...
if(!$errorMSG) {
    echo json_encode(array("message" => "Image Uploaded Successfully", "status" => true, "imageUrl" => $imageUrl));
}

$conn->close();
?>
