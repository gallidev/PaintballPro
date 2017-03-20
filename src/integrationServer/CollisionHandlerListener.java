package integrationServer;

import physics.PowerupType;

public interface CollisionHandlerListener
{
	void onFlagCaptured(int player);
	void onFlagDropped(int player);
	void onFlagRespawned(int player);
	void onPowerupAction(PowerupType type, int player);
}
