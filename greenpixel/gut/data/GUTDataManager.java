package greenpixel.gut.data;

import greenpixel.gut.anim.Animation;
import greenpixel.gut.anim.AnimationBundle;
import greenpixel.gut.anim.FrameData;
import greenpixel.gut.data.ConversationData;
import greenpixel.gut.data.ItemData;
import greenpixel.gut.data.LootDropData;
import greenpixel.gut.data.QuestData;
import greenpixel.gut.data.WaveBundle;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class GUTDataManager
{
	public static GUTDataManager instance;

	public ArrayList<String> stringArray;
	public ArrayList<ConversationData> convoArray;
	public ArrayList<QuestData> questArray;
	public ArrayList<AnimationBundle> animBundles;
	public ArrayList<String> mapVariableArray;
	public ArrayList<ItemData> itemArray;
	public ArrayList<ContainerData> conObjArray;
	public ArrayList<LootData> lootArray;
	public ArrayList<BreakableEntityData> breakableArray;
	public ArrayList<EntityData> npcDataArray;
	public ArrayList<DoorData> doorObjArray;
	public ArrayList<BarrierData> barrierObjArray;
	public ArrayList<WaveBundle> waveBundles;
	public ArrayList<String> stringVariableArray;
	public ArrayList<TriggerData> triggerArray;
	public ArrayList<String> triggerTypeArray;
	public ArrayList<String> aiTypeArray;
	public ArrayList<ButtonData> buttonArray;

	public GUTDataManager()
	{
		animBundles = new ArrayList<AnimationBundle>();
		stringArray = new ArrayList<String>();
		stringVariableArray = new ArrayList<String>();
		mapVariableArray = new ArrayList<String>();
		conObjArray = new ArrayList<ContainerData>();
		lootArray = new ArrayList<LootData>();
		breakableArray = new ArrayList<BreakableEntityData>();
		npcDataArray = new ArrayList<EntityData>();
		doorObjArray = new ArrayList<DoorData>();
		barrierObjArray = new ArrayList<BarrierData>();
		itemArray = new ArrayList<ItemData>();
		convoArray = new ArrayList<ConversationData>();
		waveBundles = new ArrayList<WaveBundle>();
		triggerTypeArray = new ArrayList<String>();
		triggerArray = new ArrayList<TriggerData>();
		aiTypeArray = new ArrayList<String>();
		questArray = new ArrayList<QuestData>();
		buttonArray = new ArrayList<ButtonData>();
	}

	public void loadGUTData(String path, JFrame parent)
	{
		try
		{
			DataInputStream in = new DataInputStream(new FileInputStream(new File(path)));

			System.out.println("Loading GUT data: " + path);

			animBundles.clear();
			stringArray.clear(); 
			stringVariableArray.clear(); 
			mapVariableArray.clear(); 
			conObjArray.clear(); 
			lootArray.clear(); 
			breakableArray.clear(); 
			npcDataArray.clear(); 
			doorObjArray.clear(); 
			barrierObjArray.clear();
			itemArray.clear(); 
			questArray.clear(); 
			convoArray.clear(); 
			waveBundles.clear(); 
			triggerTypeArray.clear();
			triggerArray.clear();
			aiTypeArray.clear();
			buttonArray.clear();

			int totalLoading = in.readInt();

			JProgressBar bar = new JProgressBar(0, totalLoading);
			bar.setStringPainted(true);

			JDialog dialog = new JDialog(parent, "Loading " + path);
			dialog.setSize(new Dimension(200, 80));
			dialog.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - dialog.getWidth()) / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - dialog.getHeight() / 2);
			dialog.add(bar);
			dialog.setVisible(true);

			int progress = 0;
			int numVariables = in.readInt();

			bar.setString("Loading: %" + (100 * bar.getValue() / totalLoading));
			bar.repaint();
			dialog.repaint();

			for (int i = 0; i < numVariables; i++)
			{
				int nameLength = in.readInt();
				String name = "";

				for (int j = 0; j < nameLength; j++)
				{
					name += in.readChar();
				}

				stringVariableArray.add(name);
			}

			int numString = in.readInt();

			for (int i = 0; i < numString; i++)
			{
				int nameLength = in.readInt();
				String name = "";

				for (int j = 0; j < nameLength; j++)
				{
					name += in.readChar();
				}

				stringArray.add(name);
				bar.setValue(++progress);
				bar.setString("Loading Strings: " + (100 * bar.getValue() / totalLoading) + "%");
				bar.repaint();
				dialog.repaint();
			}

			numVariables = in.readInt();

			for (int i = 0; i < numVariables; i++)
			{
				QuestData qData = new QuestData();	
				int nameLength = in.readInt();

				for (int j = 0; j < nameLength; j++)
				{
					qData.name += in.readChar();
				}

				nameLength = in.readInt();

				for (int j = 0; j < nameLength; j++)
				{
					qData.displayName += in.readChar();
				}

				nameLength = in.readInt();

				for (int j = 0; j < nameLength; j++)
				{
					qData.description += in.readChar();
				}

				int numObjectives = in.readInt();

				for (int j = 0; j < numObjectives; j++)
				{
					QuestObjectiveData qoData = new QuestObjectiveData();

					nameLength = in.readInt();

					for (int k = 0; k < nameLength; k++)
					{
						qoData.name += in.readChar();
					}

					nameLength = in.readInt();

					for (int k = 0; k < nameLength; k++)
					{
						qoData.displayName += in.readChar();
					}

					nameLength = in.readInt();

					for (int k = 0; k < nameLength; k++)
					{
						qoData.description += in.readChar();
					}

					qData.objectives.add(qoData);
				}

				questArray.add(qData);
			}

			numVariables = in.readInt();

			for (int i = 0; i < numVariables; i++)
			{
				ConversationData convo = new ConversationData();

				int nameLength = in.readInt();

				for (int j = 0; j < nameLength; j++)
				{
					convo.name += in.readChar();
				}

				int numDialog = in.readInt();

				for (int j = 0; j < numDialog; j++)
				{
					DialogData gd = new DialogData();

					int stringLength = in.readInt();

					for (int k = 0; k < stringLength; k++)
					{
						gd.text += in.readChar();
					}

					gd.animationGroup = in.readInt();
					gd.animationIndex = in.readInt();
					gd.textSpeed = in.readInt();
					gd.useAnimation = in.readBoolean();

					convo.dialog.add(gd);
				}

				convoArray.add(convo);
			}

			int numAnimBundles = in.readInt();

			for (int i = 0; i < numAnimBundles; i++)
			{
				int nameLength = in.readInt();
				String name = "";

				for (int j = 0; j < nameLength; j++)
				{
					name += in.readChar();
				}
				
				AnimationBundle aBundle = new AnimationBundle(name);

				int imgWidth = in.readInt();
				int imgHeight = in.readInt();
				
				BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);

				byte[] byteBuffer = new byte[imgWidth * imgHeight * 4];
				int bufferCount = 0;

				in.read(byteBuffer);

				for (int j = 0; j < imgHeight; j++)
				{
					for (int k = 0; k < imgWidth; k++)
					{
						int a = byteBuffer[bufferCount++] & 0x000000FF;
						int r = byteBuffer[bufferCount++] & 0x000000FF;
						int g = byteBuffer[bufferCount++] & 0x000000FF;
						int b = byteBuffer[bufferCount++] & 0x000000FF;

						int argb = (a << 24) | (r << 16) | (g << 8) | b;

						img.setRGB(k, j, argb);

						bar.setValue(++progress);
						bar.setString("Loading Anim " + name + ": " + (100 * bar.getValue() / totalLoading) + "%");
						bar.repaint();
						dialog.repaint();
					}
				}

				aBundle.spriteSheet = img;

				int numBounds = in.readInt();
				
				aBundle.bounds = new ArrayList<Rectangle>();
				
				for (int j = 0; j < numBounds; j++)
				{
					Rectangle r = new Rectangle(in.readInt(), in.readInt(), in.readInt(), in.readInt());
					aBundle.bounds.add(r);
				}

				int numAnims = in.readInt();
				
				aBundle.anims = new ArrayList<Animation>();
				
				for (int j = 0; j < numAnims; j++)
				{
					nameLength = in.readInt();
					name = "";

					for (int k = 0; k < nameLength; k++)
					{
						name += in.readChar();
					}

					Animation a = new Animation(name);
					
					a.type = in.readInt();
					a.loopType = in.readInt();

					int numFrames = in.readInt();

					for (int k = 0; k < numFrames; k++)
					{
						FrameData fd = new FrameData();
						fd.numDrawFrames = in.readInt();
						fd.frameIndex = in.readInt();
						fd.offsetX = in.readInt();
						fd.offsetY = in.readInt();
						fd.frameOp = in.readInt();
						fd.orientation = in.readInt();

						a.addFrame(fd);
					}

					aBundle.anims.add(a);
				}

				animBundles.add(aBundle);
			}

			numVariables = in.readInt();

			for (int i = 0; i < numVariables; i++)
			{
				int nameLength = in.readInt();
				String name = "";

				for (int j = 0; j < nameLength; j++)
				{
					name += in.readChar();
				}

				mapVariableArray.add(name);
			}

			int numItems = in.readInt();

			for (int i = 0; i < numItems; i++)
			{
				ItemData item = new ItemData();

				int nameLength = in.readInt();

				for (int j = 0; j < nameLength; j++)
				{
					item.name += in.readChar();
				}

				int descriptionLength = in.readInt();

				for (int j = 0; j < descriptionLength; j++)
				{
					item.description += in.readChar();
				}

				itemArray.add(item);
			}

			int doorObjects = in.readInt();

			for (int i = 0; i < doorObjects; i++)
			{
				int nameLength = in.readInt();

				DoorData o = new DoorData();

				for (int j = 0; j < nameLength; j++)
				{
					o.name += in.readChar();
				}

				o.mapIndex = in.readInt();
				o.xTile = in.readInt();
				o.yTile = in.readInt();
				o.closedAnimGroup = in.readInt();
				o.closedAnimIndex = in.readInt();
				o.openingAnimGroup = in.readInt();
				o.openingAnimIndex = in.readInt();
				o.openAnimGroup = in.readInt();
				o.openAnimIndex = in.readInt();
				o.lockType = in.readInt();
				o.lockGroup = in.readInt();
				o.lockIndex = in.readInt();
				o.lockMessageIndex = in.readInt();
				o.exitType = in.readInt();
				o.exitTarget = in.readInt();
				o.isVisible = in.readBoolean();
				o.collisionBox.x = in.readInt();
				o.collisionBox.y = in.readInt();
				o.collisionBox.width = in.readInt();
				o.collisionBox.height = in.readInt();
				o.doorBox.x = in.readInt();
				o.doorBox.y = in.readInt();
				o.doorBox.width = in.readInt();
				o.doorBox.height = in.readInt();
				doorObjArray.add(o);
			}

			int barrierObjects = in.readInt();

			for (int i = 0; i < barrierObjects; i++)
			{
				int nameLength = in.readInt();

				BarrierData o = new BarrierData();

				for (int j = 0; j < nameLength; j++)
				{
					o.name += in.readChar();
				}

				o.closedAnimGroup = in.readInt();
				o.closedAnimIndex = in.readInt();
				o.openingAnimGroup = in.readInt();
				o.openingAnimIndex = in.readInt();
				o.openAnimGroup = in.readInt();
				o.openAnimIndex = in.readInt();
				o.lockType = in.readInt();
				o.lockGroup = in.readInt();
				o.lockIndex = in.readInt();
				o.lockMessageIndex = in.readInt();
				o.exitType = in.readInt();
				o.exitTarget = in.readInt();
				o.isAutomatic = in.readBoolean();
				o.collisionBox.x = in.readInt();
				o.collisionBox.y = in.readInt();
				o.collisionBox.width = in.readInt();
				o.collisionBox.height = in.readInt();

				barrierObjArray.add(o);
			}

			int npcObjects = in.readInt();

			for (int i = 0; i < npcObjects; i++)
			{
				EntityData nData = new EntityData();

				int nameLength = in.readInt();

				for (int j = 0; j < nameLength; j++)
				{
					nData.name += in.readChar();
				}

				nameLength = in.readInt();

				for (int j = 0; j < nameLength; j++)
				{
					nData.displayName += in.readChar();
				}

				nData.aiType = in.readInt();
				nData.initialDirection = in.readInt();
				nData.collisionBox.x = in.readInt();
				nData.collisionBox.y = in.readInt();
				nData.collisionBox.width = in.readInt();
				nData.collisionBox.height = in.readInt();
				nData.hitBox.x = in.readInt();
				nData.hitBox.y = in.readInt();
				nData.hitBox.width = in.readInt();
				nData.hitBox.height = in.readInt();
				nData.health = in.readInt();
				nData.money = in.readInt();
				nData.experience = in.readInt();
				nData.attackCooldown = in.readInt();
				nData.defense = in.readFloat();
				nData.attack = in.readFloat();
				nData.speed = in.readFloat();
				nData.range = in.readFloat();
				nData.isMoveable = in.readBoolean();
				nData.isCollideable = in.readBoolean();
				nData.isFriendly = in.readBoolean();
				nData.isHittable = in.readBoolean();
				nData.zPos = in.readInt();

				int numAnimations = in.readInt();

				for (int j = 0; j < numAnimations; j++)
				{
					EntityAnimationData aData = new EntityAnimationData();

					int animNameLength = in.readInt();

					for (int k = 0; k < animNameLength; k++)
					{
						aData.name += in.readChar();
					}

					aData.upAnimGroup = in.readInt();
					aData.upAnimIndex = in.readInt();
					aData.downAnimGroup = in.readInt();
					aData.downAnimIndex = in.readInt();
					aData.leftAnimGroup = in.readInt();
					aData.leftAnimIndex = in.readInt();
					aData.rightAnimGroup = in.readInt();
					aData.rightAnimIndex = in.readInt();

					nData.animations.add(aData);
				}

				int dialogSize = in.readInt();

				for (int j = 0; j < dialogSize; j++)
				{
					nData.dialog.add(in.readInt());
				}

				int lootSize = in.readInt();

				for (int j = 0; j < lootSize; j++)
				{
					LootDropData lData = new LootDropData();

					lData.lootType = in.readInt();
					lData.minAmount = in.readInt();
					lData.maxAmount = in.readInt();
					lData.dropPercentage = in.readFloat();

					nData.loot.add(lData);
				}

				int questSize = in.readInt();

				for (int j = 0; j < questSize; j++)
				{
					QuestData qData = new QuestData();

					nData.quests.add(qData);
				}

				npcDataArray.add(nData);
			}

			int conObjects = in.readInt();

			for (int i = 0; i < conObjects; i++)
			{
				int nameLength = in.readInt();

				ContainerData conObj = new ContainerData();

				for (int j = 0; j < nameLength; j++)
				{
					conObj.name += in.readChar();
				}

				conObj.lockType = in.readInt();
				conObj.lockGroup = in.readInt();
				conObj.lockIndex = in.readInt();
				conObj.lockMessageIndex = in.readInt();
				conObj.openMessageIndex = in.readInt();
				conObj.closedAnimGroup = in.readInt();
				conObj.closedAnimIndex = in.readInt();
				conObj.openingAnimGroup = in.readInt();
				conObj.openingAnimIndex = in.readInt();
				conObj.openAnimGroup = in.readInt();
				conObj.openAnimIndex = in.readInt();
				conObj.exitType = in.readInt();
				conObj.exitTarget = in.readInt();
				conObj.collisionBox.x = in.readInt();
				conObj.collisionBox.y = in.readInt();
				conObj.collisionBox.width = in.readInt();
				conObj.collisionBox.height = in.readInt();

				int invSize = in.readInt();

				for (int j = 0; j < invSize; j++)
				{
					InventoryData iData = new InventoryData();
					iData.itemType = in.readInt();
					iData.amount = in.readInt();
					conObj.inventory.add(iData);
				}

				conObjArray.add(conObj);
			}

			int lootNumber = in.readInt();

			for (int i = 0; i < lootNumber; i++)
			{
				LootData lData = new LootData();

				int nameLength = in.readInt();

				for (int j = 0; j < nameLength; j++)
				{
					lData.name += in.readChar();
				}

				lData.lootAnimGroup = in.readInt();
				lData.lootAnimIndex = in.readInt();
				lData.collisionBox.x = in.readInt();
				lData.collisionBox.y = in.readInt();
				lData.collisionBox.width = in.readInt();
				lData.collisionBox.height = in.readInt();

				int inventoryNum = in.readInt();

				for (int j = 0; j < inventoryNum; j++)
				{
					InventoryData iData = new InventoryData();

					iData.itemType = in.readInt();
					iData.amount = in.readInt();

					lData.inventory.add(iData);
				}

				lootArray.add(lData);
			}

			int breakableNum = in.readInt();

			for (int i = 0; i < breakableNum; i++)
			{
				BreakableEntityData bData = new BreakableEntityData();

				int nameLength = in.readInt();

				for (int j = 0; j < nameLength; j++)
				{
					bData.name += in.readChar();
				}	

				bData.collisionBox.x = in.readInt();
				bData.collisionBox.y = in.readInt();
				bData.collisionBox.width = in.readInt();
				bData.collisionBox.height = in.readInt();
				bData.hitBox.x = in.readInt();
				bData.hitBox.y = in.readInt();
				bData.hitBox.width = in.readInt();
				bData.hitBox.height = in.readInt();
				bData.fixedAnimGroup = in.readInt();
				bData.fixedAnimIndex = in.readInt();
				bData.breakingAnimGroup = in.readInt();
				bData.breakingAnimIndex = in.readInt();
				bData.brokenAnimGroup = in.readInt();
				bData.brokenAnimIndex = in.readInt();
				bData.health = in.readInt();

				int lootNum = in.readInt();

				for (int j = 0; j < lootNum; j++)
				{
					LootDropData lData = new LootDropData();

					lData.lootType = in.readInt();
					lData.minAmount = in.readInt();
					lData.maxAmount = in.readInt();
					lData.dropPercentage = in.readFloat();

					bData.loot.add(lData);
				}

				breakableArray.add(bData);
			}

			int triggerNum = in.readInt();

			for (int i = 0; i < triggerNum; i++)
			{
				TriggerData tData = new TriggerData();

				int nameLength = in.readInt();

				for (int j = 0; j < nameLength; j++)
				{
					tData.name += in.readChar();
				}

				tData.triggerType = in.readInt();
				tData.triggerGroup = in.readInt();
				tData.triggerIndex = in.readInt();
				tData.lockType = in.readInt();
				tData.lockGroup = in.readInt();
				tData.lockIndex = in.readInt();
				tData.collisionBox.x = in.readInt();
				tData.collisionBox.y = in.readInt();
				tData.collisionBox.width = in.readInt();
				tData.collisionBox.height = in.readInt();

				triggerArray.add(tData);
			}

			int triggerTypeNum = in.readInt();

			for (int i = 0; i < triggerTypeNum; i++)
			{
				int nameLength = in.readInt();
				String name = "";

				for (int j = 0; j < nameLength; j++)
				{
					name += in.readChar();
				}

				triggerTypeArray.add(name);
			}

			int aiTypeNum = in.readInt();

			for (int i = 0; i < aiTypeNum; i++)
			{
				int nameLength = in.readInt();
				String name = "";

				for (int j = 0; j < nameLength; j++)
				{
					name += in.readChar();
				}

				aiTypeArray.add(name);
			}

			int buttonNum = in.readInt();

			for (int i = 0; i < buttonNum; i++)
			{
				ButtonData bData = new ButtonData();

				int nameLength = in.readInt();

				for (int j = 0; j < nameLength; j++)
				{
					bData.name += in.readChar();
				}

				int textLength = in.readInt();

				for (int j = 0; j < textLength; j++)
				{
					bData.text += in.readChar();
				}

				bData.upAnimGroup = in.readInt();
				bData.upAnimIndex = in.readInt();
				bData.downAnimGroup = in.readInt();
				bData.downAnimIndex = in.readInt();
				bData.selectedAnimGroup = in.readInt();
				bData.selectedAnimIndex = in.readInt();
				bData.collisionBox.x = in.readInt();
				bData.collisionBox.y = in.readInt();
				bData.collisionBox.width = in.readInt();
				bData.collisionBox.height = in.readInt();
				bData.textBox.x = in.readInt();
				bData.textBox.y = in.readInt();
				bData.textBox.width = in.readInt();
				bData.textBox.height = in.readInt();

				buttonArray.add(bData);
			}

			/*
			int waveNumber = in.readInt();

			for (int i = 0; i < waveNumber; i++)
			{
				int nameLength = in.readInt();
				String name = "";

				for (int j = 0; j < nameLength; j++)
				{
					name += in.readChar();
				}

				WaveBundle bundle = new WaveBundle(name);

				bundle.xPos = in.readInt();
				bundle.yPos = in.readInt();
				bundle.exitType = in.readInt();
				bundle.exitTarget = in.readInt();

				int numWaves = in.readInt();

				for (int j = 0; j < numWaves; j++)
				{
					WaveObject wave = new WaveObject(enemyObjArray);

					int attackGroups = in.readInt();

					for (int k = 0; k < attackGroups; k++)
					{
						wave.amounts.add(in.readInt());
						wave.enemies.add(in.readInt());
						wave.delays.add(in.readInt());
						wave.type = in.readInt();
					}

					bundle.waves.add(wave);
				}

				GUTDataManager.instance.waveBundles.add(bundle);
			}
			*/

			dialog.dispose();
			in.close();

			JOptionPane.showMessageDialog(parent, "Load Successful: " + path);		
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
}
