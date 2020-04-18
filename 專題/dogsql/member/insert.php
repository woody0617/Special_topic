<?php
header("Content-Type:text/html; charset=utf-8");
$dbhost = 'localhost';
$dbuser = 'happydog';
$dbpass = 'happy12354';
$dbname = 'app_dogface';
$conn = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);//"localhost","my_user","my_password","my_db"
//mysqli_select_db("dog ,$conn");
mysqli_query($conn, "SET NAMES UTF8"); //$con, sql

$name = $_POST['my_name'];
$phone =$_POST['my_phone'];
$mail = $_POST['my_mail'];
$pwd = $_POST['my_pwd'];

$dname =$_POST['d_name'];
$dage =$_POST['d_age'];
$dvariety =$_POST['d_variety'];
$dfeature =$_POST['d_feature'];

// $name ="y0";
// $phone ="0000978978";
// $mail = "qwe000qwe";
//  $pwd ="2554";
//
//  $dname ="來福";
//  $dage ="2016/02/29";
//  $dvariety ="就...冠廷";
//  $dfeature ="0000000";
$sql = "INSERT INTO `member_table` (`p_name`,`p_phone`,`p_email`,`p_pwd`) VALUES('$name','$phone','$mail','$pwd')";
//$result =  mysqli_query($conn, $sql); //$con, sql
$result =  mysqli_query($conn, $sql); //$con, sql

$sql = "SELECT `p_id` FROM `member_table` where `p_email`='$mail'";//查詢 id
$result = mysqli_query($conn,$sql);
while($e=mysqli_fetch_assoc($result)){
      $output[]=$e;
      //echo $e['p_id'];
    }
$a = $output[0]['p_id'];
//echo $a;
$sql = "INSERT INTO `dog_table` (`d_name`,`d_age`,`d_variety`,`d_feature`,`p_id`) VALUES('$dname','$dage','$dvariety','$dfeature','$a')";
$result =  mysqli_query($conn, $sql);

mysqli_close($conn);

//echo "Za Warudo";

// echo $name;
// echo $phone;
// echo "88888888888888888888888";
// echo $mail;
// echo $pwd;
 echo "success";

?>
