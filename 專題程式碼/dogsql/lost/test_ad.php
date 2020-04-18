<?php
header("Content-Type:text/html; charset=utf-8");
$dbhost = 'localhost';
$dbuser = 'happydog';
$dbpass = 'happy12354';
$dbname = 'app_dogface';
$conn = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);
mysqli_query($conn, "SET NAMES UTF8"); //$con, sql

    $did = $_POST['did'];
    $pid = $_POST['pid'];

    //$did = "15";
    //$pid = "20";

    $sql = "SELECT * FROM `lost_table`  where p_id='$pid' && d_id='$did'";//查詢整個表單 ex.' $ name'
    $result = mysqli_query($conn,$sql) ;//查詢
    while($e=mysqli_fetch_assoc($result)){
      $output[]=$e;
    }

    mysqli_close($conn);
    echo json_encode($output);

?>
