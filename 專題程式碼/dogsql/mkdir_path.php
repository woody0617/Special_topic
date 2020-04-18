<?php
header("Content-Type:text/html; charset=utf-8");
$dbhost = 'localhost';
$dbuser = 'happydog';
$dbpass = 'happy12354';
$dbname = 'app_dogface';
$conn = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);//"localhost","my_user","my_password","my_db"
mysqli_query($conn, "SET NAMES UTF8"); //$con, sql

$pid =$_POST['myid'];
$did = $_POST['dogid'];


// $sql = "SELECT * FROM `dog_table` where `d_id` = '$id'";
// $result =  mysqli_query($conn, $sql); //$con, sql

// while($e=mysqli_fetch_assoc($result)){
//   $output[]=$e;
// }
// $b = $output[0]['d_id'];
// $a = $output[0]['p_id'];

// $sql = "SELECT * FROM `member_table` where `p_id` = '$a'";
// $result =  mysqli_query($conn, $sql); //$con, sql

// while($e=mysqli_fetch_assoc($result)){
//   $outputp[]=$e;
// }

// $c = $outputp[0]['p_name'];

$pathname = "D$did"."_$pid";


//echo $pathname;
 $path="C:/xampp/htdocs/abc/$pathname";
//echo $path;

if (is_dir($path)){
  echo "對不起！目錄 " . $path . " 已經存在！";
 }else{
  //
  $res=mkdir(iconv("UTF-8", "GBK", $path),0777,true);
  if ($res){
   echo "目錄 $path 創建成功";
  }else{
   echo "目錄 $path 創建失敗";
  }
 }

?>
