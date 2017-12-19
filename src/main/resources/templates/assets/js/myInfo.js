$(document).ready(function () {
    var code = $.getUrlParam("code");
    //alert("code="+code);
    if (!code)
        code = "authdeny";

    $.get("/wx/codeToOpenId/" + code, function (result) {
        console.log("result=" + JSON.stringify(result));
        var wxUser = result.data;
        //alert("wxUser="+wxUser);
        var app = new Vue({
            el: '#app',
            data: {
                message: 'Hello Vue!',
                // code : $.getUrlParam('code')
                wxUser: wxUser,
                tele: wxUser.tele,
                code: " ",
                time: 60

            },
            methods: {
                getAuthCode: function () {
                    if (this.time < 60) {
                        return;
                    }

                    $.get("/NoAuthService/smsAuth/" + this.tele, function (result) {
                        console.log("===getAuthCode().result=" + JSON.stringify(result))
                        if(result.code==0)
                            alert("获取验证码失败！")
                    });

                    var me = this;
                    //me.sendMsgDisabled = true;
                    var interval = window.setInterval(function () {
                        console.log("in Interval time=" + me.time);
                        if ((me.time--) <= 0) {
                            me.time = 60;
                            window.clearInterval(interval);
                        }
                    }, 1000);


                },
                submitBind: function () {
                    ///NoAuthService/bindTele/{wxUserId}/{tele}/{code}
                    var  me=this;
                    var url = "/NoAuthService/bindTele/" + this.wxUser.id + "/" + this.tele + "/" + this.code;
                    console.log("url=" + url);
                    $.post(url, function (result) {
                        console.log(JSON.stringify(result))
                        if (result.code == 0) {
                            alert("绑定成功!");
                            me.code = "";
                            me.wxUser.tele = me.tele;
                        }
                        else
                            alert("绑定失败,请检查号码或验证码!");
                    });

                },
                // getAuthCode: function () {
                //     console.log("tele=" + this.tele);
                //     $.get("/NoAuthService/smsAuth/" + this.tele, function (result) {
                //         console.log(JSON.stringify(result))
                //     });
                // }

                submitAddInfo: function () {
                 //   /userAddressLName/{wxUserId}/{longName}/{address}
                    var url = "/NoAuthService/userAddressLName/" + this.wxUser.id + "/" + this.wxUser.longName + "/" + this.wxUser.mailAddr;
                    console.log("url=" + url);
                    $.post(url, function (result) {
                        console.log(JSON.stringify(result))
                        if (result.code == 0) {
                            alert("资料提交成功!");
                        }
                        else
                            alert("资料提交失败!"+result.data);
                    });
                }

            },
            computed: {
                teleIsUnicom: function () {
                    var isChinaUnion =/^1(3[0-2]|5[56]|8[56]|4[5]|7[6])\d{8}$/;
                    var bool= isChinaUnion.test(this.wxUser.tele);
                    console.log("bool="+bool);
                    return bool;
                },
                codeButtonTitle: function () {
                    console.log("codeButtonTitle() time=" + this.time);
                    if (this.time == 60) {
                        return "获取验证码";
                    } else {
                        return "重新获取(" + this.time + ")";
                    }
                },
                codeButtonVisable: function () {
                    if (this.teleIsUnicom)
                        return false;
                    if (!this.tele)
                        return false;
                    if (this.tele.length != 11)
                        return false;
                    if (this.tele == this.wxUser.tele)
                        return false;
                    return true;
                },
                codeButtonDisable: function () {
                    //console.log("codeButtonTitle() time=" + this.time);
                    if (this.time == 60) {
                        return false;
                    }
                    return this;
                },
                canSubmitBind: function (){
                    console.log("in canSubmitBind tele= " + this.tele + "   wxUser.tele=" + this.wxUser.tele)
                    if (!this.codeButtonVisable)
                        return false;
                    if (this.tele.length != 11)
                        return false;
                    if (this.code.length != 4)
                        return false;
                    if (this.tele == this.wxUser.tele)
                        return false;
                    return true;
                }
            }
            ,
            watch: {
                // 如果 `question` 发生改变，这个函数就会运行
                tele: function () {
                    console.log("tele.change");
                }
                ,
                code: function () {
                    console.log("code.change");
                }
            }
            ,
        })

    });
});

