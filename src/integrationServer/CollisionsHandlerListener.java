package integrationServer;

import physics.PowerupType;

public interface CollisionsHandlerListener
{
	void onFlagCaptured(int player);
	void onFlagDropped(int player);
	void onFlagRespawned(int player);
	void onPowerupAction(PowerupType type, int player);
	void onPowerupRespawn(PowerupType type, int location);
	void onShotBullet(int playerId, int bulletId, double x, double y, double angle);
	void onBulletKills(int playerId, int bulletId);

}
