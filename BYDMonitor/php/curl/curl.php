<?php
$ch = curl_init();
                curl_setopt($ch, CURLOPT_URL, "http://www.net.cn");
                curl_setopt($ch, CURLOPT_HEADER, false);
                #curl_setopt($ch, CURLOPT_TIMEOUT_MS, 30);
                #curl_setopt($ch, CURLOPT_CONNECTTIMEOUT_MS, 10000);
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
                $response = curl_exec($ch);
                $curl_errno = curl_errno($ch);
            $curl_error = curl_error($ch);
                curl_close($ch);
echo $response ;
?>

