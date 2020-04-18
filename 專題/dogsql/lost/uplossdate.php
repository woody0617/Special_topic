<?php
header("Content-Type:text/html; charset=utf-8");
$dbhost = 'localhost';
$dbuser = 'happydog';
$dbpass = 'happy12354';
$dbname = 'app_dogface';
$conn = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);
mysqli_query($conn, "SET NAMES UTF8"); //$con, sql
    $pid =$_POST['pid'];
    $did = $_POST['dogid'];
    $name = $_POST['myname'];
    $variety = $_POST['myvariety'];
    $feature = $_POST['myfeature'];
    $time =  $_POST['mytime'];
    $ad =  $_POST['myaddress'];
    $adname =  $_POST['myadname'];
    $yon = $_POST['myyon'];

    $sql = "INSERT INTO `lost_table` (`d_id`,`l_location`,`l_address`,`l_time`,`p_id`,`l_yon`) VALUES('$did','$ad','$adname','$time','$pid','$yon')";
    $result =  mysqli_query($conn, $sql);

    mysqli_close($conn);
    echo "掛失新增成功";

?>
