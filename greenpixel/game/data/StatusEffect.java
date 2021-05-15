package greenpixel.game.data;

public class StatusEffect
{
	public int type;
	public int cooldown;

	public StatusEffect()
	{
		type = 0;
		cooldown = 0;
	}

	public void init(int statusType, int statusCooldown)
	{
		type = statusType;
		cooldown = statusCooldown;
	}
}
