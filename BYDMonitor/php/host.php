<?php
// store session data
session_start();
echo "<br>SERVER_PORT: ".$_SERVER["SERVER_PORT"]."</br>\n";  
echo "<br>HTTP_HOST: ".$_SERVER["HTTP_HOST"]."</br>\n";  
echo "<br>SERVER_NAME: ".$_SERVER["SERVER_NAME"]."</br>\n";  
echo "<br>REQUEST_URI: ".$_SERVER["REQUEST_URI"]."</br>\n";  
echo "<br>PHP_SELF: ".$_SERVER["PHP_SELF"]."</br>\n";  
echo "<br>QUERY_STRING: ".$_SERVER["QUERY_STRING"]."</br>\n";  
echo "<br>HTTP_REFERER: ".$_SERVER["HTTP_REFERER"]."</br>\n";  
echo "<br>REQUEST_METHOD: ".$_SERVER['REQUEST_METHOD']."</br>\n"; 

echo "<br>POST: ".$_POST."</br>\n";  
echo "<br>HTTP_RAW_POST_DATA: ".$GLOBALS["HTTP_RAW_POST_DATA"]."</br>\n"; 
echo "<br>file_get_contents: ".file_get_contents("php://input")."</br>\n"; 

$private_key_array = array();
$private_key_array[0]='-----BEGIN PRIVATE KEY-----
MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALmqfCTAXmjo/HeO
VsP24IJWHqdoON6mrxGBw7Yv1LvYz7rhxlapvIdTt1P97oUgQMLj43eTLIUBYlVI
LJ9dUENVnTm2yJDOddjlKoEi2TAE/bpDhRAyHPvftxD9LMAjhAK5XJt8SIIgGF8W
sgautyQykv2mXQq4useqGuKKGzkTAgMBAAECgYA7I4W1gxzPV98LCeizNNDgUyQi
eL0cloVPE0FOp7+gMfhA86dkdV5a0JylgU3XU6WtLj+HF/bnSsCSn5GvFV7595SQ
jWpzs+UrTMakU4b+VwHhajEC+TAY6GmIkl7WNtXpXaJOCKGSfvfuuB+aHT+KGWgO
9jeaPob2GDlPmKvBOQJBAPiwwe5RpKym1flNRg10Vy5DKABJIXY33U2sgckMhzex
wPtpnigYD8INlCOOlubQqVHE95wYv3jEDyayaDUxej8CQQC/H4HA5d5bHg8yoAjp
YFw3pPIWqdX1lN23CORJ7OFaCzr9ihE+2wrYQTKGv/oaN4yvdGKmhWH/PBAlVH2Y
EEQtAkAbPemQfJsIWqI30/IniSX/qFWaamhi9c2lM49oAZpSTINyRuiPRct8G1at
6nG3SQYzMdtZ2xAmX3u1Znep1AirAkBc2dKwwzw+aiEaCDOsJZtHkUPnjMUclSEN
zi0SWRm2JCSZjl0Ie2QOYcZJPrUjzX70sodGvSA53pU9fYmyuZxVAkAcYNgItJ8B
9qrR1qfOElNlsywq77zzjn4r7mKhtG4cp+EeSlvCSQNfNAqxysB5HPYMMI3X+yAK
8tSeMZOC0y5u
-----END PRIVATE KEY-----';

$private_key_array[1]='-----BEGIN PRIVATE KEY-----
MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCaJBI72oB0dqXh
aooxkAHnwzPCAeA60oqJrNYNnMS1NWXC2yoFiXsfAmxEaEDtMzljlM+4Ab0D8ThF
CuU4ZDRm+/GLwq74cmuIg7tUkyEOrUntYPzuTiDW4duii/fM1sRXkfYPHVoIEa8O
Ulzr8+UemD1dKREiA50tHSU+4rOStQ8lAPn/pYQDgANSZ2KVP7iakvbq54+vKeqn
97U3A+6BgK38x8bjJRxDxpBH6H0oXR8uX1rJWOwylRoInnckJ4/6SZ9F8lI2TCDd
lsdvGw76mfxv4AT4R3vP5qDkuV7FdXSRyYyEKppjnPvIHx5HYCwL3JlvOB4uNpa4
zF1YjySBAgMBAAECggEAd4ZPCVCW2TiDyrb1sBcOEyx/vwn7WUGXoch/N5D/y9A7
N40BGcgQy+vyDl2VMxnMf5aOOA5JBnyPeKJJOqAhLlZhaFGvogH/tp0Hrz5jnODs
+YCCwTueJWKAEJxeq91B5PHPDstg2+7ygrWFToYSfdXph4rnuSt6Pthh4FCklLZT
j9JSM6d5umNJihzBiq8xetjxB2zzMgdYnueqwJnbTis2ecIULCS21y5W7yBX/1ZA
WtMVwfauuKHPlgtggMmfaLNULpnOtcJ+9ea8oMPAnYqDXH60ZA8e0PqolVo+Fpwf
XUrFIAVEcyUP40DjwuknZ9rf7uEwZiJZaz2AYBaRDQKBgQDIuPcc0mdvqMgf/oAc
2Vhj2LSZFgKn0z8Cw7iuBt2t5/q2WgOcw9ikWc39zYjDcNdg1yHVVAuCISnb0SWi
ykQ+OIPDyZ7g3Tsj6fhD3wRjXfMANUn38SZjsMZ5bRoAOnviXX32REBhjU9bvm8+
mgjN6CB3zJLCN6tkOqO5W0nonwKBgQDElxRTo2xtcB4S4nfCYmYfr+r9uDX4iIJc
D04gjOv2zOcuudSC88FX3Gd07QAiLQFj4dwRdv4mrMreWA9Gu0B6AoVgzZXvnqXq
8My67WVfxrCAZwGggaiT+k3hsrKw3b/yi0q+WV9WDrhx40mRPpUe5Ncol+Uz8tzT
sHvPuMk+3wKBgQCxS6/6pV74If2jRQmkcdbBWwkqr2ZpFw7xQjA+h5IHqoCp0Zif
P958TVbGeGX1PBcCUwRvim0f5Gs03uh/gmoFC6ZXqh/MP52de2zvhm451yJtbKSJ
fViLdfnaB6SPzxjeOfapMGAtysPuJafdg/GC7ya+qtR2+PVK8wDrGNePwQKBgCRp
w3cWfx/qM3fWAM69gSL/Z6xlMYH+bv70SslWsOI+49dQzl8PHVHT3knXGD48TdSd
YjDN9JdfTBhRAUtdpbyzuNbMXahLn3nb7l/1VuCdodW2vqNnQL1gEIrsmZKPEF2x
lT+auEDtaxaVPewsRly5kGzRbb/ojSScvOIl3QjLAoGAERKAoyZJBQ9sr0Ds4H5g
ZIgFQyKaBHiecC4HZtCufvt5Fl5RZ8uyi4xq3GC1CzJj+9PX9W4GjxulpBJJHcny
GvtKVV/hNNtltCPB30e5u+6CZCOKo6hCw2CtO4XHAd1LP21P1Lr1aPKs0QYi2RAm
WAtvr6qc+rmmwVSGEqYORzc=
-----END PRIVATE KEY-----';

$private_key_array[2]='-----BEGIN PRIVATE KEY-----
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
7r7HTJNgYl61ollVQjA/RRCI
-----END PRIVATE KEY-----';

$private_key_array[3]='-----BEGIN PRIVATE KEY-----
MIIJQwIBADANBgkqhkiG9w0BAQEFAASCCS0wggkpAgEAAoICAQCtRkwWKUyq3ofN
UByjaiw0dek9xe5pqs0bD9x81FOeBxI/das4HmgBb13t/9STYhM2eeqkca7Al5Az
ICGDBA50aNJD7KDX+8ogA75tvlPEp/MeiZNvFRkWXkrlOq7HqybAZy4WUouCsofU
4FZwM+edP7Ckx7DoI1fPjgbvyt6FmrB2RWWprYW5GcRKYP+Hh20ipN1OxINcQ8dx
Uh6zK2jAXqxIkMorofLgvLTej/liVHdAS39+MbSiCZGdzockjyRqZMNcHbHa7Mlh
4Fh/FnVEcYTyabfgOcP8eY0CTyLdRGhbqvVltDq2MmJQRIjPJJzk7OElB34ht78W
Xcc3TTUnrrz3ZmCQNaXBvJ1WSghtpFp+48fnKFhGz10PRbU46G7g65GTFORuGzpS
y/nfbNVTBxPd1OA65I46BgyuOYmtG+t+28M03vEaMLgHBZeiSKl+Du/S2CUOcCJh
FFYKw25YHQGrfN3rv3rmrhykHE8j//LyCUWrWh6O5Xx1uN1cfavnm7wy82+eJkPP
oqc31j+r/ADKoTdVGWLBGGLpUDpYs2ndmHd0O1YlDdhXC2Izq64TY5vSKNN+mVVL
zNxOlZt/VwflWrIcjnt8aZQ4U+vQi4ZDUxgfawhgkjlQkK10iO6u8LWOF8OYp6/z
ZF5+Kr5PmFfF+8g0CkqyHXP1sFzmowIDAQABAoICAQCYmAbboWVOOQkZa4q88vyY
+pn8d7n9PpjAU1iWahp1Wb7dOhAeLNDlKqFqvlkDrmRDHlzgkukV3KP2Ej7Q9ZqR
M9qwTMxdSPV4+jMSzIFS96EobHn1Ynwkcce9Vc+kMXrACF3S9MC9yeHLPAGDcIJx
viwfAWkvFij3tRkwxdTvjgoHLhhYtGLGXwW5hvCh90bKMqxe+LrCX0Jd7rbwjLME
bpuEkFYq1MVi22zhb6Nsz/vxGEvf44AzDwA5HC9xp8mnFLPjBJejt9Glq3/j8/Az
5VDydt2iztxlFNp0hSKr/9lzmPvhpDXsTDS+gYyF97Vf+ahRBucyZHIZDV8quYir
vtG4b8gtaNE2ymMt+KNhYxkdkGdPwHgsdtKbPFyN12ErMIACmdEmWXjjWVvVeksd
x+DM2TI1igbMWw8IMAqCUNdXXYaXTZXVwxYgfK/+yhDmCIH5iqkusnbpFrB66FWX
eTtJi8TuBg6oUbrzdWmWjNsEXPx4y6F4tOaniZeKofdjMRM0Znr0HxHc7VW+6I9V
NANV6oJHPod5MZek2Q94TgTnGxFIvo0Yf5KTUgZZjyGmqQquxJ+acmSutOHajkZR
cn5MqdVik4wwwDf9Quvp8NRNgvHG1/L17g4ic9b8y8YeEy4jnKG5orAxHpBr948t
GuuRrj+xvZ1fewBKVXlyCQKCAQEA/4nNuFstEQ2yzXlgykdJgceOtr+shHbwv3KV
UIMz5tIBQWfDnDSg/WNk5YDmmRY0ggUHYAlynJgl4J+OKKVUcx4oh3PDX+SaQBzc
ThPwSLQIZi/sakBkeExmqxaZb3urLUtvjNSClCJMIme9/IETDSGXhS8EU/7xm6u+
h2y2uU0hZlBm9rfzKfloh2E87zw2idK5wBRHt6qUPgVft3PxuGrdtRB9zesF6Y/z
2Q/mSpMhl+2b5u9KyZmt1dZKs1j3jvXDoU9wcpb8ArQKVrXg6qlzNanHonvhzbaW
OEeIbSD0sTSbM2x8TkPvYOxpsYGOuG5E6GNKBJUwFO/UI3daZQKCAQEArZZxhnPv
Qj9f+oJOijQBSpgUncx1dQA1SsRaLUwyj2IOJY5bHQfkK4STE3mn4arQEUlZSd3j
q3jgyYKh4KpIa0y8O68jMtBSEmESSDJfAVk6lrE6GpSuCF1WKb2wsO3RxTGACFUy
vYJQdfd0P20RpJbXvvVWc3SDtyxSZdCrDC/OGz2QBPhzklQ0lredRk5/JkjdtzKT
8w/GXlDk5DEhOWZDkjGXCnKnLsXJV1DV/2x9QsG+IIHtW0joaMs6dpK+rDrRlfZt
xqMdf+3W0R7CvckzopqWvkyNYgAY+oxJjeDj03Fk5Fxn2HrqaY4OrALIIX+JCvps
sYDYAbH5zvzoZwKCAQEA1mb91z+QuW0/50x+3FkhoKUZ5dU+X0oYRtlR1GKxCmqL
KSBxrD8wEEnz3uffEvThi3HCtV5X7nFt3mCg+xN0nr3gDX4gn/WDQmW0pkHuamG7
ZlmxR5jNc23O+p+Uj0g44GEZsBShlr6MGJXoftKhdHQJ2HIRdJREjXN1AdVCHolL
i7fMHBdNC5u6i1QXtzZTICJRPYsGfWC+zqbdn2PFnRD1AjVbCIm3xTbFxwqjE1EN
b5aqJwIBQtMRWohY1s+/rhAgWtj3JP3vZik/P2+MzfbstkrEOF0uoNYghgnmuoFb
LM9RpLzSMyymzTCuoYNthurmIAEmml++HuL2n+dFTQKCAQB06zX/56qppmIRKx+X
eru78H9CYepjKga7G1HZ3M7ZQKRiq2W3Qg9vOFaP8YsgNpr6absYQ7oLTtmpILHD
zy4EB6VpVeeRXDtLYdKcikDveZg605hdOvOVcriMWNXf9e8qsNKZyjjo6BMlwcys
w9v0Th1242ydS0dEYzgEl+39wV1Df823a1j8oHbjZT0rk8eUk9OyhwCb4G4uLLJe
+LB2JukwWPJw+RSeLl4Uz7+z0PD1u+VBpAGpUIwpuWjhYKnI+70bEuaRumAuQJZG
OD/W8OQ0i4cpGgrIjWRFraQFSgpp6qCYJ51SdEfbXPGI7j66QVqe6Fm5nRzg1214
+2PVAoIBAEWB4KyeUk7oSJ+hh4mS7gffwBEmzB4RgI9LQCSnmMtAy+boIPj8i271
WzcMhw8JlYNEBG6u8qrxDVzPYO+uOvkCEOaEebPhhiDvKmNTGuDdxxWrQfmiQJj/
Pe5FZqdRcyyENFzC5+n6BhS+W5TfXd946ySLcQ5J/QD+m5yn6QrrN6Jmx1+C6n/+
k70KGBgZYeIVMxz6KiUsec+MnAqor33Uyvl0gYwI3PyFJYIrZ5bZdKnu/aLWwho7
UK/xZCbJ5Ijydz1b/sw/BeNqYW+c2JZVamg3kZ5ZtsW1/XygIu8CrAXJq1Qkcp9P
ao1fi9ywHZ8P/PDr4DOZm7R5LhdnlfE=
-----END PRIVATE KEY-----';

$public_key_array = array();
$public_key_array[0]='-----BEGIN PUBLIC KEY-----
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5qnwkwF5o6Px3jlbD9uCCVh6n
aDjepq8RgcO2L9S72M+64cZWqbyHU7dT/e6FIEDC4+N3kyyFAWJVSCyfXVBDVZ05
tsiQznXY5SqBItkwBP26Q4UQMhz737cQ/SzAI4QCuVybfEiCIBhfFrIGrrckMpL9
pl0KuLrHqhriihs5EwIDAQAB
-----END PUBLIC KEY-----';

$public_key_array[1]='-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmiQSO9qAdHal4WqKMZAB
58MzwgHgOtKKiazWDZzEtTVlwtsqBYl7HwJsRGhA7TM5Y5TPuAG9A/E4RQrlOGQ0
Zvvxi8Ku+HJriIO7VJMhDq1J7WD87k4g1uHboov3zNbEV5H2Dx1aCBGvDlJc6/Pl
Hpg9XSkRIgOdLR0lPuKzkrUPJQD5/6WEA4ADUmdilT+4mpL26uePrynqp/e1NwPu
gYCt/MfG4yUcQ8aQR+h9KF0fLl9ayVjsMpUaCJ53JCeP+kmfRfJSNkwg3ZbHbxsO
+pn8b+AE+Ed7z+ag5LlexXV0kcmMhCqaY5z7yB8eR2AsC9yZbzgeLjaWuMxdWI8k
gQIDAQAB
-----END PUBLIC KEY-----';

$public_key_array[2]='-----BEGIN PUBLIC KEY-----
MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAqRA7HYj/2iy7SeB6UcXu
sATIdJ3EWV0PbkKpVULe6O7Q8LdAZ4uQhvWjX8FLn3PHabzmu8s49V8MLCaRXGlR
fkjUcV/fvNBYoi1rk/TNF3Go/WDbUj6sbl/ZaOPavYNrnTX2GcYDcVgAlMXVpOGk
raBOXmp7rfGezN8bOgYywusERyMGyItFxdWeVRjcBH8MuvCwGPfk8J1paC3a5tbN
nV4nHm6NVtFCuXJIJ08dKaB15ls62uJec44iUbEGsnhY5t0tL7chqEj7cxGCnZGA
xQ775iZnAg8afS9AgawD/TUseMw0n7lvG7LuP4XhHb+rFtDIN88SZKTREw2yaf9P
62++Xnzb26dd6TYsstU0sRuFJVDym3Lx2hASV6c9TcZ25EZkg7PKs7bv4O1Tlg0e
OPel9VZxALoMGgSEoAkWrEvUmN5+ZBPCKPdekSZgFA3IT0AYZagLS2ZuI+0vgj5z
d5t+t8sN41tiUe4T/LHwW8Vnh7y7b27b+wqG88V2PjdZAgMBAAE=
-----END PUBLIC KEY-----';

$public_key_array[3]='-----BEGIN PUBLIC KEY-----
MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEArUZMFilMqt6HzVAco2os
NHXpPcXuaarNGw/cfNRTngcSP3WrOB5oAW9d7f/Uk2ITNnnqpHGuwJeQMyAhgwQO
dGjSQ+yg1/vKIAO+bb5TxKfzHomTbxUZFl5K5Tqux6smwGcuFlKLgrKH1OBWcDPn
nT+wpMew6CNXz44G78rehZqwdkVlqa2FuRnESmD/h4dtIqTdTsSDXEPHcVIesyto
wF6sSJDKK6Hy4Ly03o/5YlR3QEt/fjG0ogmRnc6HJI8kamTDXB2x2uzJYeBYfxZ1
RHGE8mm34DnD/HmNAk8i3URoW6r1ZbQ6tjJiUESIzySc5OzhJQd+Ibe/Fl3HN001
J66892ZgkDWlwbydVkoIbaRafuPH5yhYRs9dD0W1OOhu4OuRkxTkbhs6Usv532zV
UwcT3dTgOuSOOgYMrjmJrRvrftvDNN7xGjC4BwWXokipfg7v0tglDnAiYRRWCsNu
WB0Bq3zd67965q4cpBxPI//y8glFq1oejuV8dbjdXH2r55u8MvNvniZDz6KnN9Y/
q/wAyqE3VRliwRhi6VA6WLNp3Zh3dDtWJQ3YVwtiM6uuE2Ob0ijTfplVS8zcTpWb
f1cH5VqyHI57fGmUOFPr0IuGQ1MYH2sIYJI5UJCtdIjurvC1jhfDmKev82Refiq+
T5hXxfvINApKsh1z9bBc5qMCAwEAAQ==
-----END PUBLIC KEY-----';

$post_data = file_get_contents("php://input");
$post_data_array = explode('&',$post_data);
$size = 0;
$private_key = "";
for($index=0;$index<count($post_data_array);$index++) 
{ 
	$pos = strpos($post_data_array[$index],'=');
	if($pos ==false)
	{
		errlog('error link:'.$post_data)
		return;
	}
	echo 'pos:'.$pos."\n";
	
	$name = substr($post_data_array[$index],0,$pos);
	$val = substr($post_data_array[$index],$pos+1);
	//list($name, $val) = split ('=', $post_data_array[$index]); 
	echo $name." ".$val."\n";
	
	if($name == "sz")
	{
		$private_key = getKey((int)$val);
	}	
	if($name=="data")
	{
		$encrypt_data = base64_decode($val,true);
		$key = openssl_pkey_get_private($private_key);
		openssl_private_decrypt($encrypt_data,$decrypt_data,$key,OPENSSL_PKCS1_PADDING);
		echo $decrypt_data."\n";
	
		$key = openssl_pkey_get_private($private_key);
		openssl_private_encrypt($decrypt_data,$dat,$key,OPENSSL_PKCS1_PADDING);
		$dat = base64_encode($dat);
		echo "datass=".$dat;
	}	
} 

echo "\nfinish\n";
//database
$con = mysql_connect("qdm157633487.my3w.com","qdm157633487","lijianzhang");
if (!$con)
{
	errlog('Could not connect: ' . mysql_error());
	die();
}

mysql_select_db("qdm157633487_db", $con);
if (mysql_query("create table if not exists test2(server_time BIGINT, client_time BIGINT);",$con))
{
	echo "table created";
}
else
{
	errlog( "Error creating database: " . mysql_error());
}

function getKey($len) {
	global $private_key_array;
	if (($len <= 0) || ($len > (128 * 4 - 11))) {
		return null;
	}
	$idx = (($len + 11) % 128) == 0 ? ($len + 11) / 128 - 1 : ($len + 11) / 128;
	$idx = (int)$idx;
    return $private_key_array[$idx]; 
}

function errlog($str)
{
  error_log($str . "\n",3,'err.log');	
}

function private_str2pem($str)
{
	$str = chunk_split($str, 64, "\n");
	$str = "-----BEGIN PRIVATE KEY-----\n".$str."-----END PRIVATE KEY-----";
	return $str;
}

?>
