<?php
    header("Content-Type:text/html; charset=utf-8");
    $dbhost = 'localhost';
    $dbuser = 'happydog';
    $dbpass = 'happy12354';
    $dbname = 'app_dogface';
    $conn = mysqli_connect($dbhost, $dbuser, $dbpass,$dbname) ;//連接資料庫
    mysqli_query($conn, "SET NAMES UTF8"); //$con, sql
    $pid =$_POST['pid'];
    $did = $_POST['did'];
    $findtime = $_POST['mytime'];
    $findad = $_POST['myad'];
    $findlocat = $_POST['mylocat'];

    $sql = "UPDATE `lost_table` SET `f_location` = '$findlocat', `f_address` = '$findad' ,`f_time` = '$findtime' ,`l_yon` = '1' where p_id='$pid' && d_id='$did'";//修改資料表單 ex.' $ name'
    mysqli_query($conn,$sql) ;
    mysqli_close($conn);
    echo "感謝您的協助";


    ?>