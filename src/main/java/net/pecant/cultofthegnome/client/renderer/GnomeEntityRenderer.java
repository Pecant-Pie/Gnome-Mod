package net.pecant.cultofthegnome.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.pecant.cultofthegnome.CultOfTheGnome;
import net.pecant.cultofthegnome.client.models.GnomeEntityModel;
import net.pecant.cultofthegnome.entities.GnomeEntity;

public class GnomeEntityRenderer extends MobRenderer<GnomeEntity, GnomeEntityModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(CultOfTheGnome.MOD_ID, "textures/entity/gnome_entity.png");
    public GnomeEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new GnomeEntityModel(ctx.bakeLayer(GnomeEntityModel.LAYER_LOCATION)), 0.15f); // this num is shadow size
    }

    @Override
    public ResourceLocation getTextureLocation(GnomeEntity entity) {
        return TEXTURE;
    }
}
