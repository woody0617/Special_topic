<?php
header("Content-Type:text/html; charset=utf-8");
$dbhost = 'localhost';
$dbuser = 'happydog';
$dbpass = 'happy12354';
$dbname = 'app_dogface';
$conn = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);
mysqli_query($conn, "SET NAMES UTF8"); //$con, sql

    $pid = $_POST['myid'];

    //$did = "28";
    //$pid = "20";

    $sql = "SELECT * FROM `lost_table`  where p_id='$pid'";//查詢整個表單 ex.' $ name'
    $result = mysqli_query($conn,$sql) ;//查詢
    $num = mysqli_num_rows($result);
    if($num >0){
      while($e=mysqli_fetch_assoc($result)){
        $output[]=$e;
      }
      //讀取該主人掛失的所有狗id
      $i = count($output);
      $a ="";
      $allid = "";
      for ($j = 0;$j<$i ;$j++){
        $a = $output[$j]['d_id']."/";
        $allid=$allid.$a;
      }
      //各狗的id切割
      $sp_allid = explode("/",$allid);
      $b = count($sp_allid);
      $array = array();
      for ($j = 0;$j<$i ;$j++){
        $c = $sp_allid[$j];
        $sql = "SELECT * FROM `dog_table`  where d_id='$c'";
        $result = mysqli_query($conn,$sql) ;
        while($e=mysqli_fetch_assoc($result)){
          $op[]=$e;
        }
      }
      $i = count($op);
      $a ="";
      for ($j = 0;$j<$i ;$j++){
        echo $op[$j]['d_name']."/";
      }
    }else{
      echo "nolost";
    }
    
    mysqli_close($conn);
?>
