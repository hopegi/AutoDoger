package suncere.androidapp.autobasemodule;

public class ResultDataHandlerContext {

	private PluginContext pluginContext;
	private NetDataHandlerContext netDataHandlerContext;
	private boolean isCache;
	
	public boolean isCache() {
		return isCache;
	}
	public void setCache(boolean isCache) {
		this.isCache = isCache;
	}
	public PluginContext getPluginContext() {
		return pluginContext;
	}
	public void setPluginContext(PluginContext pluginContext) {
		this.pluginContext = pluginContext;
	}
	public NetDataHandlerContext getNetDataHandlerContext() {
		return netDataHandlerContext;
	}
	public void setNetDataHandlerContext(NetDataHandlerContext netDataHandlerContext) {
		this.netDataHandlerContext = netDataHandlerContext;
	}
}
