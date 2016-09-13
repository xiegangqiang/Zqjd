Ext.define('SystemApp.View.Orders', {
    extend: 'Ext.panel.Panel',
    xtype: 'orders',
    
    initComponent: function() {
    	
    	Ext.apply(this, {
    		border: 0,
            layout: 'border',
            items: [this.createGrid()]
        });
        this.createWindows();
        this.callParent(arguments);
    },
    
    createGrid: function() {
    	 var store = Ext.create('Ext.data.Store', {
    		pageSize: 25,
    	    fields:['id', 'userId', 'name', 'phone', 'address', 'ordernumber', 'flowsteprec', 'modifyDate', 'posts', 'operator'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/orders.do?list', 
    	        reader: {
    	            type: 'json',
    	            root: 'datas',
    	            totalProperty: 'total'
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
    	    	text: '订单号',
    	    	dataIndex: 'ordernumber',
    	    	flex: 0.15
    	    }, { 
    	    	text: '电话号码',  
    	    	dataIndex: 'phone',
    	    	flex: 0.1
    	    }, {
    	    	text: '客户姓名',
    	    	dataIndex: 'name',
    	    	flex: 0.1
    	    }, {
    	    	text: '送货地址',
    	    	dataIndex: 'address',
    	    	flex: 0.2
    	    }, {
    	    	text: '服务岗位',
    	    	dataIndex: 'posts',
    	    	flex: 0.2
    	    }, {
    	    	text: '操作人',
    	    	dataIndex: 'operator',
    	    	flex: 0.1
    	    }, {
    	    	text: '最后修改时间',
    	    	dataIndex: 'modifyDate',
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
                	text : "微信通知",  
                    cls: 'icon-news', 
                    scope: this,
                    handler: this.sendNotice
                }, {  
                    text : "编辑",  
                    cls: 'icon-edit', 
                    scope: this,
                    handler: this.editGrid
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
            	text: '添加订单',
            	iconCls: 'icon-add',
            	scope: this,
            	handler: this.addGrid
            }, '->', {
            	xtype: 'textfield',
            	width: 180,
            	emptyText: '输入电话号码搜索'
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
			phone: me.previousSibling('textfield').getValue()
		};
		store.load();
    },
    
    createWindows: function() {
    	
    	this.userStore = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'phone', 'name', 'address'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/orders.do?users'
    	    },
            autoLoad: true
    	});
    	
    	this.roleStore = Ext.create('Ext.data.ArrayStore', {
	        fields: ['id','name'],
	        proxy: {
    	        type: 'ajax',
    	        url: '/admin/admin.do?role'
    	    },
    	     autoLoad: true
	    });
    	
    	this.editWin = Ext.create('Ext.window.Window', {
    		title: '编辑管理员信息',
    		border: 0,
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
    	        	xtype: 'hiddenfield',
    	        	name: 'userId'
    	        }, {
    	        	xtype: 'hiddenfield',
    	        	name: 'nextstep'
    	        }, {
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 20,
		        	items: [{
	    	        	xtype: 'combo',
	    	        	name: 'phone',
	    	        	fieldLabel: '电话号码',
	    	        	regex: /^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\d{8}$/,
	        			regexText: '请输入正确的手机号码',
	        			emptyText: '输入11位联系电话',
	    	            store: this.userStore,
	    	            queryMode: 'remote',
	    	            displayField: 'phone',
	    	            valueField: 'phone',
	    	            //editable: false,
	    	            columnWidth: 0.7,
	    	            allowBlank: false,
	    	            listeners: {
	    	            	scope: this,
			            	change: function(me, newValue, oldValue) {
			            		var form = this.editWin.down('form').getForm();
			            		var store = me.getStore();
			            		store.proxy.extraParams = {
									phone: newValue
								};
								//store.load();
								form.findField('userId').setValue('');
			            		me.expand();
			            	},
			            	select: function(me, rec) {
			            		var form = this.editWin.down('form').getForm();
			            		form.findField('userId').setValue(rec[0].get('id'));
			            		form.findField('name').setValue(rec[0].get('name'));
			            		form.findField('address').setValue(rec[0].get('address'));
			            	}
			            }
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　请输入电话搜索客户</span>'
		        	}]
    	        }, {
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 20,
		        	items: [{
	    	        	xtype: 'textfield',
	    	            name: 'ordernumber',
	    	            fieldLabel: '订单号',
	    	            columnWidth: 0.6,
	    	            disabled: true,
	    	            allowBlank: false,
	    	            margin: '0 10 0 0',
	    	            emptyText: '输入订单号供客户查询'
	    	        }, {
		        		xtype: 'checkbox',
		        		checked: true,
						boxLabel: '系统生成',
						listeners: {
							change: function(me, newValue, oldValue) {
								if(newValue) me.previousSibling('textfield').setDisabled(true);
								else me.previousSibling('textfield').setDisabled(false);
							}
						}
		        	}]
    	        }, {
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 20,
		        	items: [{
	    	        	xtype: 'textfield',
	    	            name: 'name',
	    	            fieldLabel: '姓　名',
	    	            columnWidth: 0.5,
	    	            emptyText: '客户姓名'
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　请输入客户姓名</span>'
		        	}]
    	        }, {
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 20,
		        	items: [{
	    	        	xtype: 'textfield',
	    	            name: 'address',
	    	            fieldLabel: '送货地址',
	    	            columnWidth: 0.8,
	    	            emptyText: '客户送货地址'
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　请输入客户送货地址</span>'
		        	}]
    	        }, {
    	        	xtype: 'itemselector',
		            name: 'roles',
		            anchor: '100%',
		            fieldLabel: '分配岗位',
		            store: this.roleStore,
		            displayField: 'name',
		            valueField: 'id',
		            value: [],
		            buttonsText: {top: "移到最上层", up: "向上移动", add: "选中该项", remove: "删除该项", down: "向下移动", bottom: "移到最低层"},
		            msgTarget: 'side',
		            fromTitle: '未选的岗位',
		            toTitle: '已选的岗位'
    	        }]
    	    }],
	        buttons: [{ 
	        	text: '确定',
	        	scope: this,
	        	handler: this.submitFormDate
	        }, {
	        	text: '清除所有岗位',
	        	scope: this,
	        	handler: this.clearSelected
	        }, { 
	        	text: '取消',
	        	scope: this,
	        	handler: function() {
	        		this.editWin.hide();
	        	}
	        }]
    	});
    },
    
    clearSelected: function() {
    	var field = this.editWin.down('form').getForm().findField('roles');
    	if(!field.disabled){
    		field.clearValue();
    	}
    },
    
    submitFormDate: function() {
    	var grid  = this.grid;
    	var form = this.editWin.down('form');
    	var win = this.editWin;
    	var roles = form.getForm().findField('roles').getValue();
    	if(roles.length === 0) {
    		Ext.example.msg('提示', "订单至少选一个服务岗位");
    		return;
    	}
    	form.submit({
    		url: '/admin/orders.do?save',
    	    success: function(form, action) {
    	       win.hide();
    	       grid.getStore().reload();
    	       Ext.example.msg('提示', action.result.title);
    	    },
    	    failure: function(form, action) {
    	    	Ext.Msg.alert('提示', action.result.title);
    	    }
    	});
    },
    
    addGrid: function() {
		var form = this.editWin.down('form');
		this.editWin.show();
		form.getForm().reset();
		var roleIds = [];
		this.roleStore.each(function(item, index) {
			roleIds[index] = item.get('id');
		});
		form.getForm().findField('roles').setValue(roleIds);
    },
    
    sendNotice: function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
    	var grid  = this.grid;
    	Ext.MessageBox.confirm('确认', '是否向该微信用户推送订单消息？', function(btn) {
    		if (btn != 'yes') return;
			Ext.Ajax.request({
	    	    url: '/admin/orders.do?notice',
	    	    params: {
	    			order: rec.get('id')
	    		},
	    	    success: function(response){
	    	    	var res = Ext.decode(response.responseText);
					if (res.success) {
	        	    	grid.getStore().reload();
	        	    }
	        	    Ext.example.msg('提示', res.title);
	    	    },
	    	    failure: function(response) {
	    	    	Ext.Msg.alert('提示', '操作异常');
	    	    }
	    	});
    	});
    },
    
    editGrid: function(grid, rowIndex, colIndex) {
    	var form = this.editWin.down('form');
    	this.editWin.show();
    	var rec = grid.getStore().getAt(rowIndex);
    	form.loadRecord(rec);
    	var roles = [];
    	Ext.Ajax.request({
    	    url: '/admin/orders.do?posts',
    	    params: {
    			flowsteprec: rec.get('flowsteprec')
    		},
    	    success: function(response){
    	    	var res = Ext.decode(response.responseText);
    	    	for (var i = 0; i < res.length; i++) {
    	    		roles[i] = res[i].posts;
    	    	}
    	    	form.getForm().findField('roles').setValue( roles );
    	    },
    	    failure: function(response) {
    	    	Ext.Msg.alert('提示', '操作异常');
    	    }
    	});
    },
    
    delGrid: function(grid, rowIndex, colIndex) {
    	var record = grid.getStore().getAt(rowIndex);
    	var grid  = this.grid;
		Ext.MessageBox.confirm('确认', '删除该订单后无法恢复，请确认?', function(btn) {
    		if (btn != 'yes') return;
    		Ext.Ajax.request({
        	    url: '/admin/orders.do?delete',
        	    params: {
        	        id: record.get('id')
        	    },
        	    success: function(response){
        	    	var res = Ext.decode(response.responseText);
        	    	if (res.success) {
        	    		grid.getStore().reload();
        	    	}
        	    	Ext.example.msg('提示', res.title);
        	    },
        	    failure: function(response) {
        	    	var res = Ext.decode(response.responseText);
        	    	Ext.Msg.alert('提示', res.title);
        	    }
        	});
		});
    },
    
    onDestroy: function(){
    	this.editWin.destroy();
        this.callParent(arguments);
    }
});