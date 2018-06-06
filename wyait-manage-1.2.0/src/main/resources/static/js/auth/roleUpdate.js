/**
 * 角色授权
 */
$(function() {

    if(flag=="updateRole"){
        // zTree 的参数配置
        var setting = {
            check: {
                enable: true,
                chkboxType:{ "Y":"p", "N":"s"}
            },
            data: {
                simpleData: {
                    enable: true
                }
            }
        };
        $.fn.zTree.init($("#treeDemo"), setting, permIdList);
    }

    layui.use(['form' ,'layer'], function(){
        var form = layui.form;
        var layer=layui.layer;

        //监听提交
        form.on('submit(updateRoleSumbit)', function(data){
            //获取选中的权限
            var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
            var nodes = treeObj.getCheckedNodes(true);
            //选中的复选框
            var nodeIds =new Array();
            for (var i = 0; i < nodes.length; i++) {
                nodeIds.push(nodes[i].id);
            }
            //校验是否授权
            var permList = nodeIds.join(",");
            // console.log("permList:"+permList)
            if(permList==null || permList==''){
                layer.alert("请给该角色添加权限菜单！")
                return false;
            }
            $("#rolePermIds").val(permList);
            $.ajax({
                type: "POST",
                data: $("#updateRoleForm").serialize(),
                url: "/auth/setRole",
                success: function (data) {
                    if (data == "ok") {
                        layer.alert("操作成功",function(){
                            layer.closeAll();
                            load();
                        });
                    } else {
                        layer.alert(data);
                    }
                },
                error: function (data) {
                    layer.alert("操作请求错误，请您稍后再试");
                }
            });
            return false;
        });
        form.render();
    });
});



