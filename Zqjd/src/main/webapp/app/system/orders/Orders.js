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
    	    fields:['id', 'user', 'name', 'phone', 'address', 'ordernumber', 'flowsteprec', 'modifyDate'],
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
    	    	flex: 0.2
    	    }, { 
    	    	text: '电话号码',  
    	    	dataIndex: 'phone',
    	    	flex: 0.15
    	    }, {
    	    	text: '客户姓名',
    	    	dataIndex: 'name',
    	    	flex: 0.15
    	    }, {
    	    	text: '送货地址',
    	    	dataIndex: 'address',
    	    	flex: 0.15
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
            	emptyText: '输入订单号搜索'
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
			name: me.previousSibling('textfield').getValue()
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
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 20,
		        	items: [{
	    	        	xtype: 'combo',
	    	        	name: 'department',
	    	        	fieldLabel: '输入电话',
	    	            store: this.userStore,
	    	            queryMode: 'remote',
	    	            displayField: 'phone',
	    	            valueField: 'phone',
	    	            //editable: false,
	    	            columnWidth: 0.7,
	    	            allowBlank: false,
	    	            listeners: {
			            	change: function(me) {
			            		var store = me.getStore();
			            		store.proxy.extraParams = {
									phone: me.getValue()
								};
								store.load();
			            		me.expand();
			            	},
			            	select: function(me, index) {
			            		
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
	    	            name: 'name',
	    	            fieldLabel: '姓　名',
	    	            columnWidth: 0.5
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
	    	            name: 'name',
	    	            fieldLabel: '送货地址',
	    	            columnWidth: 0.5
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
    	form.submit({
    		url: '/admin/orders.do?save',
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
    
    addGrid: function() {
		var form = this.editWin.down('form');
		this.editWin.show();
		var roleIds = [];
		this.roleStore.each(function(item, index) {
			roleIds[index] = item.get('id');
		});
		form.getForm().findField('roles').setValue(roleIds);
    },
    
    onDestroy: function(){
    	this.editWin.destroy();
        this.callParent(arguments);
    }
});