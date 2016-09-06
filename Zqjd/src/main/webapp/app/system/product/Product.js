Ext.define('SystemApp.View.Product', {
    extend: 'Ext.panel.Panel',
    xtype: 'product',
    
    initComponent: function() {
    	Ext.apply(this, {
    		border: 0,
            layout: 'border',
            items: [this.createGrid()]
        });
    	this.createEditWindows();
    	this.createImageUpload();
        this.callParent(arguments);
    },
    
    
    createGrid: function() {
    	var store = Ext.create('Ext.data.Store', {
    		pageSize: 25,
    	    fields:['name','productClass','content','img','discript','level','visible','modifyDate','productClassName'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/product.do?list', 
    	        reader: {
    	            type: 'json',
    	            root: 'datas',
    	            totalProperty: 'total'
    	        }
    	    },
            autoLoad: true
    	});
    	
    	this.grid = Ext.create('Ext.grid.Panel', {
    		border: 0,
    	    store: store,
    	    region: 'center',
    	    columnLines: true,
    	    dockedItems: [{
    	        xtype: 'pagingtoolbar',
    	        store: store,  
    	        dock: 'bottom',
    	        displayInfo: true
    	    }],
    	    columns: [{
    	    	text: '产品名称',
    	    	dataIndex: 'name',
    	    	flex: 0.15
    	    }, {
    	    	text: '所属分类',
    	    	dataIndex: 'productClassName',
    	    	flex: 0.1
    	    }, {
    	    	text: '等级排序',
    	    	dataIndex: 'level',
    	    	flex: 0.08
    	    }, {
    	    	text: '是否显示',
    	    	dataIndex: 'visible',
    	    	flex: 0.08,
    	    	renderer: function(v) {
                	if (v) return "<font color=green>显示</font>";
                	else return "<font color=red>隐藏</font>";
                }
    	    }, {
    	    	text: '产品图片',
    	    	dataIndex: 'img',
    	    	flex: 0.08,
    	    	renderer: function(v, meta){
    	    		meta.tdAttr = "data-qtip='<img src=\"" + v + "\" />'";
    	    		return "查看图片";
    	    	}
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
            	flex: 0.15,
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
            tbar: [{ 
            	xtype: 'button', 
            	text: '刷新',
            	iconCls: 'icon-refresh',
            	scope: this,
            	handler: this.refreshGird
            }, { 
            	xtype: 'button', 
            	text: '添加',
            	iconCls: 'icon-add',
            	scope: this,
            	handler: this.addGrid
            }, '->' ,{
            	xtype: 'textfield',
            	width: 180,
            	emptyText: '输入名称搜索',
            	id: 'Product.SearchName'
            },{ 
            	xtype: 'button', 
            	text: '搜索',
            	iconCls: 'icon-search',
            	scope: this,
            	handler: this.searchGrid
            }]
    	});
    	
    	return this.grid;
    },
    
    refreshGird: function() {
    	this.grid.getStore().reload();
    },
    
    searchGrid: function() {
    	var store = this.grid.getStore();
    	store.currentPage = 1;
    	store.proxy.extraParams = {
			name: Ext.getCmp('Product.SearchName').getValue()
		};
		store.load();
    },
    
    createEditWindows: function() {
    	
    	var enabled = Ext.create('Ext.data.Store', {
    	    fields: ['text', 'value'],
    	    data : [{
    	    	"text":"显示", "value": true
    	    },{
    	    	"text":"隐藏", "value": false
    	    }]
    	});
    	
    	this.productClassStore = Ext.create('Ext.data.Store', {
    	    fields: ['name', 'id'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/product.do?productClasses'
    	    },
            autoLoad: true
    	});
    	
    	this.editWin = Ext.create('Ext.window.Window', {
    	    title: '编辑新闻内容信息',
    	    border: 0,
    	    height: 600,
    	    width: 900,
    	    layout: 'border',
    	    modal: true,
    	    closeAction: 'hide',
    	    items: [{  
    	    	xtype: 'form',
    	    	region: 'center',
    	    	bodyPadding: 10,
    	    	autoScroll: true,
    	    	fieldDefaults: {
    	            labelWidth: 80,
    	            labelAlign: 'left',
    	            anchor: '100%'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        }, {
    	        	xtype: 'combo',
    	        	name: 'productClass',
    	        	fieldLabel: '产品分类',
    	            store: this.productClassStore,
    	            queryMode: 'remote',
    	            displayField: 'name',
    	            valueField: 'id',
    	            editable: false,
    	            allowBlank: false
    	        }, {
    	        	xtype: 'textfield',
    	        	name: 'name',
    	        	fieldLabel: '产品名称',
    	        	alowBlank: false
    	        }, {
    	        	xtype: 'numberfield',
    	        	name: 'level',
    	        	fieldLabel: '等级排序',
    	            minValue: 0,
    	            maxValue: 1000,
    	            allowBlank: false,
    	            value: 0
    	        }, {
    	        	xtype: 'container',
    	        	layout: 'column',
    	        	style: 'margin-bottom:5px',
    	        	id: 'Product_Container_img',
    	        	items: [{
    	        		xtype: 'textfield',
        	            name: 'img',
        	            fieldLabel: '门户端图片',
        	            columnWidth: 0.7,
        	            listeners: {
        	            	change: this.scanImage
        	            }
    	        	}, {
    	        		xtype: 'button',
    	        		text: '上传图片',
    	        		columnWidth: 0.15,
    	        		style: 'margin-left:3px',
    	        		id: 'Product_Upload_img',
    	        		scope: this,
    	        		handler: this.uploadImage
    	        	}, {
    	        		xtype: 'button',
    	        		text: '预览图片',
    	        		columnWidth: 0.15,
    	        		scope: this,
    	        		style: 'margin-left:3px'
    	        	}]
    	        }, {
    	        	xtype: 'combo',
    	        	name: 'visible',
    	        	fieldLabel: '是否显示',
    	        	store: enabled,
    	            queryMode: 'local',
    	            valueField: 'value',
    	            displayField: 'text',
    	            editable: false,
    	            allowBlank: false
    	        }, {
    	        	xtype: 'kindeditor',
    	        	name: 'content',
    	        	fieldLabel: '产品内容',
    	        	height: 250
    	        }, {
    	        	xtype: 'textarea',
    	        	name: 'discript',
    	        	fieldLabel: '产品描述',
    	        	height: 250
    	        }],
    	        buttons: [{ 
    	        	text: '确定',
    	        	scope: this,
    	        	handler: this.submitFormData
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
    
    scanImage: function(fn) {
    	var targetForm = fn.up('form').getForm();
    	Ext.getCmp('Product_Container_' + fn.name).items.get(2).setTooltip(
        	'<img src="' + targetForm.findField(fn.name).getValue() + '"  width="100%"/>'
    	);
    },
    
    createImageUpload: function() {
    	this.uploadImageWin = Ext.create('Ext.window.Window', {
    	    title: '上传图片',
    	    height: 150,
    	    width: 400,
    	    layout: 'border',
    	    modal: true,
    	    closeAction: 'hide',
    	    items: [{  
    	    	xtype: 'form',
    	    	region: 'center',
    	    	bodyPadding: 10,
    	    	fieldDefaults: {
    	            labelWidth: 75,
    	            labelAlign: 'left',
    	            anchor: '100%'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        }, {
    	            xtype: 'filefield',
    	            name: 'upload',
    	            fieldLabel: '图片文件',
    	            msgTarget: 'side',
    	            buttonText: '选择图片...'
    	        }],
    	        buttons: [{ 
    	        	text: '确定',
    	        	scope: this,
    	        	handler: this.uploadImageForm
    	        }, { 
    	        	text: '取消',
    	        	scope: this,
    	        	handler: function() {
    	        		this.uploadImageWin.hide();
    	        	}
    	        }]
    	    }]
    	});
    },
    
    uploadImage: function(fn) {
    	var form = this.uploadImageWin.down('form');
    	this.uploadImageWin.show();
    	form.getForm().reset();
    	form.getForm().findField('id').setValue(fn.id.substr(fn.id.lastIndexOf('_') + 1));
    },    

    
    uploadImageForm: function() {
    	var targetForm = this.editWin.down('form').getForm();
    	var form = this.uploadImageWin.down('form');
    	var targetName = form.getForm().findField('id').getValue();  
    	var win = this.uploadImageWin;
    	form.submit({
    		url: '/admin/upload.do?save',
    	    success: function(form, action) {
    	       targetForm.findField(targetName).setValue(action.result.msg);
    	       win.hide();
    	       Ext.Msg.alert('提示', action.result.title);
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
    },
    
    editGrid: function(grid, rowIndex, colIndex) {
    	var form = this.editWin.down('form');
    	this.editWin.show();
    	var rec = grid.getStore().getAt(rowIndex);
    	form.loadRecord(rec);
    },
    
    submitFormData: function() {
    	var grid  = this.grid;
    	var form = this.editWin.down('form');
    	var win = this.editWin;
    	form.submit({
    		url: '/admin/product.do?save',
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
		Ext.MessageBox.confirm('确认', '是否删除该产品信息?', function(btn) {
    		if (btn != 'yes') return;
    		Ext.Ajax.request({
        	    url: '/admin/product.do?delete',
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
    
    onDestroy: function(){
    	this.editWin.destroy();
    	this.uploadImageWin.destroy();
        this.callParent(arguments);
    }

});