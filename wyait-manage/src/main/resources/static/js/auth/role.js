/**
 * 更新角色
 */
//选中的复选框
var nodeIds = [];
$(function() {

    layui.use(['form' ,'tree','layer'], function(){
        var form = layui.form;
        var layer=layui.layer;

        //监听提交
        getTreeData();

        form.on('submit(roleSubmit)', function(data){
            //校验 TODO
            var array = new Array();
            //获取选中的权限id
            for(var i=0;i<$('.nodeCheck').length;i++){
                array.push($($('.nodeCheck').get(i)).attr("permid"));
            }
            //校验是否授权
            var permIds = array.join(",");
            // console.log("permIds"+permIds)
            if(permIds==null || permIds==''){
                layer.alert("请给该角色添加权限菜单！")
                return false;
            }
            $("#permIds").val(permIds);

            $.ajax({
                type: "POST",
                data: $("#roleForm").serialize(),
                url: "/auth/addRole",
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
function getTreeData() {
    $.ajax({
        type: "get",
        url: "/auth/findPerms",
        success: function (data) {
            if (data !=null) {
                initTree(data);
            } else {
                layer.alert(data);
            }
        },
        error: function () {
            layer.alert("获取数据错误，请您稍后再试");
        }
    });
}

function initTree(data){

    layui.tree({
        elem: '#perm', //指定元素
        target: '_blank', //是否新选项卡打开（比如节点返回href才有效）
        check: 'checkbox', //勾选风格
        checked: function(item) {//复选框
            //layer.msg('check当前节名称：'+ item.name + '<br>全部参数：'+ JSON.stringify(item));
            // console.log('item is Array：'+ item instanceof Array);
            // console.log('item is：'+ item);
            // console.log('check当前节名称：'+ JSON.stringify(item));
            //判断是选中还是移除选中 ,checkbox: ['&#xe626;', '&#xe627;'] //复选框
            /*var checkFlag = data.elem.checked;
            var cFlag = $(this).checked;
            console.log("checkFlag:"+checkFlag)
            console.log("cFlag:"+cFlag)*/
            //当前节点
            //nodeIds.push(item.id);
           /* if( item.children.length > 0 ){
                nodeIds= getChildNode(item);
            }
            console.log('nodeIds：'+ JSON.stringify(nodeIds));
            // permArray.add(item);
            $.unique(nodeIds);
            console.log('check当前节名称：'+ JSON.stringify(nodeIds));*/
            //校验 TODO
            //$("#permIds").val(permIds);
        },
        click: function(item){ //点击节点回调
            //layer.msg('click当前节名称：'+ item.name + '<br>全部参数：'+ JSON.stringify(item));
            // console.log('click当前节名称：'+ JSON.stringify(item));
            //treeIds+=item.id;
        },
        skin:'shihuang',//皮肤
        //checkboxName: 'permCheck',//复选框的name属性值
        //checkboxStyle: "color: #FD482C",//设置复选框的样式，必须为字符串，css样式
        /* change: function (item){//当当前input发生变化后所执行的回调//console.log(item);
             resourceIds=item;
         },
         data: {//为元素添加额外数据，即在元素上添加data-xxx="yyy"，可选
             hasChild: true
         }*/
        nodes:listToTreeJson(data)
    });
}
/**
 * 获取所有子节点的id数组
 * @param obj
 * @returns {Array}
 */
function getChildNode( obj ){
    if(obj!=null){
        if( obj.children.length > 0 ){
            $.each( obj.children, function(k, v){
                //console.log( v.id );
                nodeIds.push( v.id );
                getChildNode( v );
            });
        }
    }
    return nodeIds;
}
var demoData=[
    {"id":"aaa","pid":"account","spType":0,"layerId":0,"seqId":1,"name":"阿萨德发多少","deleted":"0"},
    {"id":"account","pid":"","spType":0,"layerId":0,"seqId":50,"name":"账户","deleted":"0"},
    {"id":"bbb","pid":"account","spType":0,"layerId":0,"seqId":2,"name":"阿萨德发多少","deleted":"0"},
    {"id":"ccc","pid":"account","spType":0,"layerId":0,"seqId":3,"name":"a啊都是发","deleted":"0"},
    {"id":"ddd","pid":"dispatch","spType":0,"layerId":0,"seqId":1,"name":"大夫","deleted":"0"},
    {"id":"dispatch","pid":"","spType":0,"layerId":0,"seqId":2,"name":"通知公告","deleted":"0"},
    {"id":"eee","pid":"dispatch","spType":0,"layerId":0,"seqId":2,"name":"；卡萨丁","deleted":"0"},
    {"id":"fff","pid":"gridding","spType":0,"layerId":0,"seqId":1,"name":"拉收到了","deleted":"0"},
    {"id":"gridding","pid":"","spType":0,"layerId":0,"seqId":1,"name":"网格化管理","deleted":"0"},
    {"id":"portals","pid":"","spType":0,"layerId":0,"seqId":3,"name":"综合信息门户管理","deleted":"0"}
];

/**
 * list转化为tree结构的json数据
 */
function listToTreeJson(data){
    //data不能为null，且是数组
    if(data!=null && (data instanceof Array)){
        //递归转化
        var getJsonTree=function(data,parentId){
            var itemArr=[];
            for(var i=0;i < data.length;i++){
                var node=data[i];
                if(node.pId==parentId && parentId!=null){
                    var newNode={name:node.name,spread:true,id:node.id,pid:node.pId,children:getJsonTree(data,node.id)};
                    itemArr.push(newNode);
                }
            }
            return itemArr;
        }
        // return JSON.stringify(getJsonTree(data,''));
        return getJsonTree(data,0);
    }
    //console.log(JSON.stringify(getJsonTree(data,'')));
}
