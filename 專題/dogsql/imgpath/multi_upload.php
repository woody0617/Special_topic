<?php
   
   $file_path = "C:/xampp/htdocs/docker/third_who_dog/";
   
   try {
    $did = $_POST['did'];
    $pid = $_POST['pid'];
    $c =  $_POST['variety'];
    $pathname = "D$did"."_$pid";
    //$file_path = $file_path.$c."/tf_files/train/".$pathname."/". basename( $_FILES['uploaded_file']['name']);
    $file_path = "C:/xampp/htdocs/docker/third_who_dog/".$c."/tf_files/train/".$pathname."/". basename( $_FILES['uploaded_file']['name']);
    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
        echo "success";
    } else{
        echo "fail";
    }
    
    } catch (RuntimeException $e) {
    echo $e->getMessage();

}


// try {
//     $file_path = $file_path . basename( $_FILES['uploaded_file'], $pathname);
//     if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
//         echo "success";
//     } else{
//         echo "fail";
//     }
    

// } catch (RuntimeException $e) {

//     echo $e->getMessage();

// }

?>