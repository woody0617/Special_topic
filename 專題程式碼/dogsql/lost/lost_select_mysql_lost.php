<?php
    header("Content-Type:text/html; charset=utf-8");
    $dbhost = 'localhost';
    $dbuser = 'happydog';
    $dbpass = 'happy12354';
    $dbname = 'app_dogface';
    $conn = mysqli_connect($dbhost, $dbuser, $dbpass,$dbname) ;//連接資料庫
    mysqli_query($conn, "SET NAMES UTF8"); //$con, sql

    $pid = $_POST['mypid'];
    $did = $_POST['mydid'];
    // $pid  = "20";
    // $did = "28";
    $sql = "SELECT * FROM `lost_table`  where p_id='$pid' && d_id='$did'";
    $result = mysqli_query($conn,$sql) ;
    while($e=mysqli_fetch_assoc($result)){
      $output[]=$e;
    }

    echo json_encode($output);
    mysqli_close($conn);
?>
