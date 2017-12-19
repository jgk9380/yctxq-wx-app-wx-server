 function      teleIsUnicom (tele) {
     var isChinaUnion =/^1(3[0-2]|5[56]|8[56]|4[5]|7[6])\d{8}$/;
     var bool= isChinaUnion.test(tele);
     console.log("bool="+bool);
     return bool;
 };

console.log(teleIsUnicom("15651554341"));