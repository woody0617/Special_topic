<?php
header("Content-Type:text/html; charset=utf-8");
$dbhost = 'localhost';
$dbuser = 'happydog';
$dbpass = 'happy12354';
$dbname = 'app_dogface';
$conn = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);
mysqli_query($conn, "SET NAMES UTF8"); //$con, sql

$a =$_POST['myid'];
$dname =$_POST['d_name'];
$dage =$_POST['d_age'];
$dvariety =$_POST['d_variety'];
$dfeature =$_POST['d_feature'];

//  $dname ="來福";
//  $dage ="2016/02/29";
//  $dvariety ="就...冠廷";
//  $dfeature ="0000000";

$sql = "INSERT INTO `dog_table` (`d_name`,`d_age`,`d_variety`,`d_feature`,`p_id`) VALUES('$dname','$dage','$dvariety','$dfeature','$a')";
$result =  mysqli_query($conn, $sql);

mysqli_close($conn);

 echo "insert_success";

?>
