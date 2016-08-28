Ext.define('SystemApp.View.Admin', {
    extend: 'Ext.panel.Panel',
    xtype: 'admin',
    
    initComponent: function() {
    	
    	Ext.apply(this, {
    		border: 0,
            layout: 'border',
            items: [this.createGrid()]
        });
    	this.createEditWindows();
    	this.webScoketHandle();
        this.callParent(arguments);
    },
    
    
    createGrid: function() {
    	var store = Ext.create('Ext.data.Store', {
    		pageSize: 25,
    	    fields:['username', 'email', 'name', 'department', 'loginIp', 'isAccountEnabled', 'loginDate', 'departmentName', 'online'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/admin.do?list', 
    	        reader: {
    	            type: 'json',
    	            root: 'datas',
    	            totalProperty: 'total'
    	        }
    	    },
            autoLoad: true
    	});
    	
    	this.departmentStore = Ext.create('Ext.data.Store', {
    	    fields: ['name', 'id'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/department.do?parent'
    	    },
    	    listeners: {
    	    	load: function(me, records, successful, eOpts ) {
    	    		me.remove(me.getAt(0));
    	    	}
    	    },
            autoLoad: true
    	});
    	
    	this.grid = Ext.create('Ext.grid.Panel', {
    	    store: store,
    	    border: 0,
    	    region: 'center',
    	    columnLines: true,
    	    emptyText: '暂无数据可显示',
    	    dockedItems: [{
    	        xtype: 'pagingtoolbar',
    	        store: store,  
    	        dock: 'bottom',
    	        displayInfo: true
    	    }],
    	    columns: [{ 
    	    	text: '用户名',  
    	    	dataIndex: 'username',
    	    	flex: 0.15
    	    }, {
    	    	text: '用户名称',
    	    	dataIndex: 'name',
    	    	flex: 0.15
    	    }, {
    	    	text: '电子邮箱',
    	    	dataIndex: 'email',
    	    	flex: 0.15
    	    }, {
    	    	text: '所属部门',
    	    	dataIndex: 'departmentName',
    	    	flex: 0.2
    	    }, {
    	    	text: '是否可用',
    	    	dataIndex: 'isAccountEnabled',
    	    	flex: 0.08,
    	    	renderer: function(v) {
                	if (v) return "<font color=green>激活</font>";
                	else return "<font color=red>失效</font>";
                }
    	    }, {
    	    	text: '是否在线',
    	    	dataIndex: 'online',
    	    	flex: 0.08,
    	    	renderer: function(v) {
                	if (v) return "<font color=green>在线</font>";
                	else return "<font color=red>离线</font>";
                }
    	    }, {
    	    	text: '最后登录IP',
    	    	dataIndex: 'loginIp',
    	    	flex: 0.15
    	    }, {
    	    	text: '最后登录时间',
    	    	dataIndex: 'loginDate',
    	    	flex: 0.15,
    	    	renderer: function(v){
    	    		if (v == null) return "";
    	    		return Ext.Date.format(new Date(v.time), 'Y-m-d H:i');
 	            }
    	    }, {
            	xtype: 'actiontextcolumn',
            	text: '主要操作',
            	flex: 0.25,
                items : [{  
                    text : "编辑",  
                    cls: 'icon-edit', 
                    scope: this,
                    handler: this.editGrid
                }, {
                	text : "[强制下线]",  
                    cls: '', 
                    scope: this,
                    handler: function(grid, rowIndex, colIndex) {
                    	var rec = grid.getStore().getAt(rowIndex);
                    	Ext.MessageBox.confirm('确认', '是否强制下线该人员', function(btn) {
    						if (btn != 'yes') return;
                    		webScoket.send('{"type": 2, "fromUser": "' + userInfo.id + '", "toUser": "' + rec.get('id') + '", "date": "' + Ext.Date.format(new Date(), 'Y-m-d H:i:s') + '", "content": ""}');
                    		Ext.Msg.alert("提示", rec.get('name')+"已下线");
                    	});
                    }
                }, {  
                    text : "删除",  
                    cls: 'icon-del',
                    scope: this,
                    handler: this.delGrid
                }]  
            }],
            tbar: ['-', { 
            	xtype: 'button', 
            	text: '刷新',
            	iconCls: 'icon-refresh',
            	scope: this,
            	handler: this.refreshGird
            }, '-', { 
            	xtype: 'button', 
            	text: '添加',
            	iconCls: 'icon-add',
            	scope: this,
            	handler: this.addGrid
            }, '->', {
	        	xtype: 'combo',
	        	name: 'online',
	        	width: 80,
	        	store: Ext.create('Ext.data.Store', {
		    	    fields: ['text', 'value'],
		    	    data : [{
		    	    	"text":"不限", "value": ''
		    	    }, {
		    	    	"text":"在线", "value": true
		    	    },{
		    	    	"text":"离线", "value": false
		    	    }]
		    	}),
	            queryMode: 'local',
	            valueField: 'value',
	            displayField: 'text',
	            editable: false,
	            value: ''
	        }, '-', {
	        	xtype: 'combo',
	        	name: 'department',
	        	width: 180,
	            store: this.departmentStore,
	            queryMode: 'remote',
	            displayField: 'name',
	            valueField: 'name',
	            emptyText: '输入、选择部门',
	            //editable: false,
	            listeners: {
	            	focus: function(me) {
	            		me.expand();
	            	}
	            }
	        }, '-', {
            	xtype: 'textfield',
            	width: 180,
            	emptyText: '输入用户名、名称搜索'
            }, '-', { 
            	xtype: 'button', 
            	text: '搜索',
            	iconCls: 'icon-search',
            	scope: this,
            	handler: this.searchGrid
            }, '-']
    	});
    	
    	return this.grid;
    },
    
    refreshGird: function() {
    	this.grid.getStore().reload();
    },
    
    searchGrid: function(me) {
    	var store = this.grid.getStore();
    	store.currentPage = 1;
    	store.proxy.extraParams = {
			name: me.previousSibling('textfield').getValue(),
			departmentName: me.previousSibling('combo').getValue(),
			online: me.previousSibling('combo').previousSibling('combo').getValue()
		};
		store.load();
    },
    
    createEditWindows: function() {
    	var enabled = Ext.create('Ext.data.Store', {
    	    fields: ['text', 'value'],
    	    data : [{
    	    	"text":"激活", "value": true
    	    },{
    	    	"text":"失效", "value": false
    	    }]
    	});
    	
    	Ext.apply(Ext.form.field.VTypes, {
            password: function(val, field) {
                if (field.initialPassField) {
                    var pwd = field.up('form').down('#' + field.initialPassField);
                    return (val == pwd.getValue());
                }
                return true;
            },
            passwordText: '登录密码与确认密码不一致'
        });
    	
    	this.editWin = Ext.create('Ext.window.Window', {
    	    title: '编辑管理员信息',
    	    height: 500,
    	    width: 700,
    	    layout: 'border',
    	    modal: true,
    	    closeAction: 'hide',
    	    items: [{  
    	    	xtype: 'form',
    	    	region: 'center',
    	    	bodyPadding: 10,
    	    	fieldDefaults: {
    	           labelWidth: 70,
    	           labelAlign: 'left',
    	           anchor: '100%',
    	           labelStyle: 'color:green;padding-left:4px'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        }, {
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'textfield',
	    	            name: 'username',
	    	            fieldLabel: '用户名',
	    	            allowBlank: false
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　请输入登陆名，只支付英文、数字、符号</span>'
		        	}]
    	        }, {
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'textfield',
	    	        	inputType: 'password',
	    	            fieldLabel: '登录密码',
	    	            name: 'password',
	    	            itemId: 'pass',
	    	            listeners: {
	    	                validitychange: function(field){
	    	                	field.up('container').nextSibling().items.get(0).validate();
	    	                    //field.next().validate();
	    	                },
	    	                blur: function(field){
	    	                	field.up('container').nextSibling().items.get(0).validate();
	    	                    //field.next().validate();
	    	                }
	    	            }
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　设置用户登陆密码</span>'
		        	}]
    	        }, {
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'textfield',
	    	        	inputType: 'password',
	    	            fieldLabel: '确认密码',
	    	            name: 'pass-cfrm',
	    	            vtype: 'password',
	    	            initialPassField: 'pass' 
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　再次输入密码以确认</span>'
		        	}]
    	        }, {
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'textfield',
	    	            name: 'name',
	    	            fieldLabel: '用户名称'
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　请输入用户名称</span>'
		        	}]
    	        }, {
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'textfield',
	                    fieldLabel: '邮箱地址',
	                    vtype: 'email',
	                    name: 'email'
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　请输入联系邮箱地址</span>'
		        	}]
    	        }, {
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'combo',
	    	        	name: 'department',
	    	        	fieldLabel: '直属部门',
	    	            store: this.departmentStore,
	    	            queryMode: 'remote',
	    	            displayField: 'name',
	    	            valueField: 'id',
	    	            editable: false,
	    	            allowBlank: false,
	    	            listeners: {
	    	            	expand: function(me, eOpts) {
	    	            		me.getStore().reload();
	    	            	}
	    	            }
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　请选择部门</span>'
		        	}]
    	        }, {
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'combo',
	    	        	name: 'isAccountEnabled',
	    	        	fieldLabel: '是否可用',
	    	        	store: enabled,
	    	            queryMode: 'local',
	    	            valueField: 'value',
	    	            displayField: 'text',
	    	            editable: false,
	    	            value:true,
	    	            allowBlank: false
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　设置该用户的使用状态</span>'
		        	}]
    	        }, {
    	            xtype: 'checkboxgroup',
    	            fieldLabel: '角色权限',
    	            columns: 6,
    	            id: 'Admin_Roles',
    	            vertical: true,
    	            items: []
    	        }, {
    	        	xtype: 'textarea',
    	        	name: 'remark',
    	        	fieldLabel: '备注说明',
    	        	height: 50
    	        }],
    	        buttons: [{ 
    	        	text: '确定',
    	        	scope: this,
    	        	handler: this.submitFormDate
    	        }, { 
    	        	text: '取消',
    	        	scope: this,
    	        	handler: function() {
    	        		this.editWin.hide();
    	        	}
    	        }]
    	    }]
    	});
    },
    
    addGrid: function() {
		var form = this.editWin.down('form');
    	this.editWin.show();
    	form.getForm().reset();
    	Ext.getCmp('Admin_Roles').removeAll();
    	Ext.Ajax.request({
    	    url: '/admin/admin.do?role',
    	    params: {
    	    	adminId: ""
    	    },
    	    success: function(response){
    	    	var res = Ext.decode(response.responseText);
    	    	for (var i = 0; i < res.length; i++) {
    	    		Ext.getCmp('Admin_Roles').add([{ boxLabel: res[i].name, inputValue: res[i].id, checked: res[i].checked }]);
    	    	}
    	    },
    	    failure: function(response) {
    	    	Ext.Msg.alert('提示', '操作异常');
    	    }
    	});
    },
    
    editGrid: function(grid, rowIndex, colIndex) {
    	var form = this.editWin.down('form');
    	this.editWin.show();
    	var rec = grid.getStore().getAt(rowIndex);
    	form.loadRecord(rec);
    	Ext.getCmp('Admin_Roles').removeAll();
    	Ext.Ajax.request({
    	    url: '/admin/admin.do?role',
    	    params: {
    	    	adminId: rec.get('id')
    	    },
    	    success: function(response){
    	    	var res = Ext.decode(response.responseText);
    	    	for (var i = 0; i < res.length; i++) {
    	    		Ext.getCmp('Admin_Roles').add([{ boxLabel: res[i].name, inputValue: res[i].id, checked: res[i].checked }]);
    	    	}
    	    },
    	    failure: function(response) {
    	    	Ext.Msg.alert('提示', '操作异常，服务器可能没响应');
    	    }
    	});
    	
    	
    },
    
    submitFormDate: function() {
    	
    	var roles = [];
    	var checkeds = Ext.getCmp('Admin_Roles').getChecked();
    	for (var i = 0; i < checkeds.length; i++) {
    		roles.push(checkeds[i].inputValue);
    	}
    	
    	var grid  = this.grid;
    	var form = this.editWin.down('form');
    	var win = this.editWin;
    	form.submit({
    		url: '/admin/admin.do?save',
    		params: {
    			roles: roles
    		},
    	    success: function(form, action) {
    	       win.hide();
    	       Ext.Msg.alert('提示', action.result.title);
    	       grid.getStore().reload();
    	    },
    	    failure: function(form, action) {
    	    	Ext.Msg.alert('提示', action.result.title);
    	    }
    	});
    },
    
    delGrid: function(grid, rowIndex, colIndex) {
    	var record = grid.getStore().getAt(rowIndex);
    	var grid  = this.grid;
		Ext.MessageBox.confirm('确认', '是否删除该管理员信息?', function(btn) {
    		if (btn != 'yes') return;
    		Ext.Ajax.request({
        	    url: '/admin/admin.do?delete',
        	    params: {
        	        id: record.get('id')
        	    },
        	    success: function(response){
        	    	var res = Ext.decode(response.responseText);
        	    	if (res.success) {
        	    		grid.getStore().reload();
        	    	}
        	    	Ext.Msg.alert('提示', res.title);
        	    },
        	    failure: function(response) {
        	    	var res = Ext.decode(response.responseText);
        	    	Ext.Msg.alert('提示', res.title);
        	    }
        	});
		});
    },
    
    webScoketHandle: function() {
    	 var store = this.grid.getStore();
    	 webScoket.onmessage = function (event) {
            var msgJson = eval("("+event.data+")");
            
            if(msgJson.toUser === "ALL" && msgJson.type < 2) {
	            setTimeout(function() {
	            	for(var i=0;i<store.totalCount;i++) {
						if(store.getAt(i).get('id') == msgJson.fromUser) {
							if(msgJson.type == 0) store.getAt(i).set("online", true)
							else if(msgJson.type == 1) store.getAt(i).set("online", false);
						}
	            	}
	            }, 500);
	            Ext.getCmp('app-header').items.get(1).items.get(6).setValue(msgJson.content);
            }
            
            if(msgJson.toUser === userInfo.id && msgJson.type === 2) {alert('提示，你已被管理员强制下线');location.href = "/admin/logout";}
         };
    },
    
    onDestroy: function(){
    	this.editWin.destroy();
        this.callParent(arguments);
    }

});