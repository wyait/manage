/**
 * 修改用户密码
 * */
//获取路径uri
var picCode;
$(function(){
    picCode = drawPic();
    //监控（发送验证码）
    layui.use(['form' ,'layer'], function() {
        var form = layui.form;
        var layer = layui.layer;
        //监控提交
        form.on("submit(getMsg)",function (data) {
            //sendMsg();
            var flag=checkData();
            if(flag!=false){
                sendMessage(this,true);
            }
            return false;
        });
        //确认手机号
        form.on("submit(updatePwd)",function () {
            updatePwd();
            return false;
        });
        //确认修改密码
        form.on("submit(setPwd)",function () {
            setPwd();
            return false;
        });
    })
})
function checkData(){
//  校验
    var mobile=$("#telephone").val();
    var code=$("#picCode").val();
    if("ok"!=ValidateUtils.checkMobile(mobile)){
        //tips层-右
        layer.tips(ValidateUtils.checkMobile(mobile), '#telephone', {
            tips: [2, '#78BA32'], //还可配置颜色
            tipsMore: true
        });
        return false;
    }
    if("ok"!=ValidateUtils.checkPicCode(code)){
        //tips层-右
        layer.tips(ValidateUtils.checkPicCode(code), '#canvas', {
            tips: [2, '#78BA32'], //还可配置颜色
            tipsMore: true
        });
        return false;
    }
    if(picCode.toLowerCase()!=code.toLowerCase()){
        //tips层-右
        layer.tips("请您输入正确的验证码", '#canvas', {
            tips: [2, '#78BA32'], //还可配置颜色
            tipsMore: true
        });
        return false;
    }
}
var wait=60;
function sendMessage(o,flag){
    if (!flag) {
        return false;
    }
    //第一次秒数
    if (wait == 60) {
        o.setAttribute("disabled", true);
        //自定义验证规则
        $.post("/user/sendMessage", {"mobile":$("#telephone").val(),"picCode":$("#picCode").val()}, function (data) {
            console.log("data:" + data)
            if (data.code == "1000") {
                layer.msg("发送短信成功");
            } else {
                picCode = drawPic();
                $("#picCode").val("");
                layer.alert(data.message);
                //禁用发送短信验证码按钮
                o.removeAttribute("disabled");
                //o.value = "获取验证码";
                wait = 60;
                flag = false;
            }
            return false;
        });
    }
    if (wait == 0) {
        o.removeAttribute("disabled");
        $("#getMsgBtn").html("获取验证码");
        wait = 60;
    } else {
        o.setAttribute("disabled", true);
        if (wait <60) {
            $("#getMsgBtn").html("<span style='margin-left: -12px;'>"+wait + "s后可重新发送</span>");
        }
        wait--;
        setTimeout(function () {
            if (wait == 0) {
                flag = true
            };
            send(o, flag)
        }, 1000)
    }
}
function updatePwd(){
    var flag=checkData();
    if(flag!=false){
        var mobileCode=$("#mobileCode").val();
        if("ok"!=ValidateUtils.checkCode(mobileCode)){
            //tips层-右
            layer.tips("请您输入正确的验证码", '#getMsgBtn', {
                tips: [2, '#78BA32'], //还可配置颜色
                tipsMore: true
            });
            return false;
        }
        $.post("/user/updatePwd",{"mobile":$("#telephone").val(),"picCode":$("#picCode").val(),"mobileCode":mobileCode},function(data){
            console.log("data:"+data)
            if(data.code=="1000"){
                layer.closeAll();
                layer.open({
                    type:1,
                    title: "设置新密码",
                    fixed:false,
                    resize :false,
                    shadeClose: true,
                    area: ['450px'],
                    content:$('#pwdDiv')
                });
            }else{
                picCode=drawPic();
                $("#picCode").val("");
                $("#mobileCode").val("");
                layer.alert(data.message);
            }
        });

    }
}
function setPwd(){
    var pwd=$("#pwd").val();
    var isPwd=$("#isPwd").val();
    if(pwd!=isPwd){
        //tips层-右
        $("#isPwd").val("");
        $("#isPwd").val("");
        layer.tips("两次输入的密码不一致", '#isPwd', {
            tips: [2, '#78BA32'], //还可配置颜色
            tipsMore: true
        });
        return false;
    }
    if("ok"!=ValidateUtils.checkSimplePassword(pwd) || "ok"!=ValidateUtils.checkSimplePassword(isPwd)){
        //tips层-右
        $("#pwd").val("");
        $("#pwd").val("");
        $("#isPwd").val("");
        $("#isPwd").val("");
        layer.alert("密码格式有误，请您重新输入");
        return false;
    }
    $.post("/user/setPwd",{"pwd":pwd,"isPwd":isPwd},function(data){
        console.log("data:"+data);
        if(data.code=="1000"){
            layer.alert("操作成功",function () {
                layer.closeAll();
                window.location.href="/logout";
            });
        }else{
            layer.alert(data.message,function () {
                layer.closeAll();
                //window.location.href="/index";
            });
        }
    });
}
