Ext.define('SystemApp.View.Orders', {
    extend: 'Ext.panel.Panel',
    xtype: 'orders',
    
    initComponent: function() {
    	
    	Ext.apply(this, {
    		border: 0,
            layout: 'border',
            items: [this.createGrid()]
        });
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
    
    onDestroy: function(){
        this.callParent(arguments);
    }
});