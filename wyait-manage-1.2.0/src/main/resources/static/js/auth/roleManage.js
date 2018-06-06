/**
 * 角色管理
 */
$(function() {
    $("#roleListLi").click(function () {
        $("#setRoleLi").removeClass("layui-this");
        $("#setRoleDiv").removeClass("layui-show");

        $("#updateRoleLi").css("display","none");
        $("#updateRoleDiv").css("display","none");
        $("#roleListLi").addClass("layui-this");
        $("#roleListDiv").addClass("layui-show");
    });
    $("#setRoleLi").click(function () {
        $("#roleListLi").removeClass("layui-this");
        $("#roleListDiv").removeClass("layui-show");

        $("#updateRoleLi").css("display","none");
        $("#updateRoleDiv").css("display","none");
        $("#setRoleLi").addClass("layui-this");
        $("#setRoleDiv").addClass("layui-show");
    });
    if(flag=="updateRole"){
        $("#roleListLi").removeClass("layui-this");
        $("#setRoleLi").removeClass("layui-this");
        $("#roleListDiv").removeClass("layui-show");
        $("#setRoleDiv").removeClass("layui-show");

        $("#updateRoleLi").addClass("layui-this");
        $("#updateRoleDiv").addClass("layui-show");
        $("#updateRoleLi").css("display","inline-block");
        $("#updateRoleDiv").css("display","inline-block");
    }
});

/**
 * 进入角色管理界面
 */
function load(){
    window.location.href="/auth/roleManage";
}