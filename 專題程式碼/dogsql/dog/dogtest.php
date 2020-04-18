<?php
    header("Content-Type:text/html; charset=utf-8");
    $dbhost = 'localhost';
    $dbuser = 'happydog';
    $dbpass = 'happy12354';
    $dbname = 'app_dogface';
    $conn = mysqli_connect($dbhost, $dbuser, $dbpass,$dbname) ;//連接資料庫
    mysqli_query($conn, "SET NAMES UTF8"); //$con, sql

    $id = $_POST['myid'];
    //$id = "21";

    $sql = "SELECT * FROM `dog_table`  where p_id='$id'";//查詢整個表單 ex.' $ name'
    $result = mysqli_query($conn,$sql) ;//查詢
    while($e=mysqli_fetch_assoc($result)){
      $output[]=$e;
    }
    $i = count($output);
    $a ="";
    for ($j = 0;$j<$i ;$j++){
      echo $output[$j]['d_name']."/";
    }
    //echo json_encode($output);
    mysqli_close($conn);
?>
