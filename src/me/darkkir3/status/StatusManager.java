package me.darkkir3.status;

import java.util.HashMap;

public final class StatusManager 
{
	private static HashMap<StatusTypes, Class<? extends BaseStatusProc>> statusMap = new HashMap<StatusTypes,Class<? extends BaseStatusProc>>();
	
	/**Creates a new copy of the specified status proc
	 * @param statusType
	 * @return
	 */
	public static BaseStatusProc getStatusProcForStatusType(StatusTypes statusType)
	{
		if(statusMap.isEmpty())
		{
			return null;
		}
		
		Class<? extends BaseStatusProc> classToInstantiate = statusMap.get(statusType);
		try 
		{
			return classToInstantiate.newInstance();
		} 
		catch (InstantiationException | IllegalAccessException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
