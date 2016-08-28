Ext.define('SystemApp.View.Help',{
	extend: 'Ext.panel.Panel',
    xtype: 'help',
    
    initComponent: function() {
    	Ext.apply(this, {
    		border: 0,
            layout: 'fit',
            items: [this.createView()]
        });
        this.callParent(arguments);
    },
    
    createView: function() {
    	var field = null;
    	if(Ext.getCmp('Tab_help').title === '考评系统使用手册') field = 'normHelp.doc';
    	
    	this.view = Ext.create('Ext.panel.Panel', {
 	    	border: 0,
		 	overflowY: 'auto',
 	    	autoScroll: true,
 	    	html: '<iframe src="https://view.officeapps.live.com/op/view.aspx?src=http://'+window.location.host+'/admin/doc/'+field+'" frameborder="0" width="100%" height="100%"></iframe>'
 	    	/*autoLoad: {
     			url : '/admin/normMark.do?tasklist',
     			scripts:true,
     			params: {
     				url: '/admin/normMark.do?tasklist'
     			}
     		}*/
    	});
	    
	    return this.view;
    },
    
    onDestroy: function(){
       this.callParent(arguments);
    }
    
})