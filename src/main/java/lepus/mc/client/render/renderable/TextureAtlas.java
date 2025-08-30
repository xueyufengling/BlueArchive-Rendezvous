package lepus.mc.client.render.renderable;

import lepus.mc.resources.ResourceLocations;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TextureAtlas implements Cloneable {

	@Override
	public Texture clone() {
		try {
			return (Texture) super.clone();
		} catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private Texture atlas;

	public TextureAtlas(ResourceLocation loc) {
		this.atlas = Texture.of(loc);
	}

	public TextureAtlas(Texture atlas) {
		this.atlas = atlas;
	}

	public TextureAtlas(String loc) {
		this.atlas = Texture.of(loc);
	}

	public int height() {
		return atlas.imageHeight();
	}

	public int width() {
		return atlas.imageWidth();
	}

	public final Texture clip(float x1, float y1, float width, float height) {
		return atlas.clip(x1, y1, width, height);
	}

	public final Texture clip(int x1, int y1, int width, int height) {
		return atlas.clip(x1, y1, width, height);
	}

	public static class EquidistantTextureAtlas extends TextureAtlas {
		/**
		 * 最左上角sprite的左上角像素相对于图像左上角的x坐标位移
		 */
		public final float begin_offset_x;
		/**
		 * 最左上角sprite的左上角像素相对于图像左上角的y坐标位移
		 */
		public final float begin_offset_y;
		/**
		 * 最右下角sprite的右下角像素相对于图像左上角的x坐标位移
		 */
		public final float end_offset_x;
		/**
		 * 最右下角sprite的右下角像素相对于图像左上角的y坐标位移
		 */
		public final float end_offset_y;
		/**
		 * 左右相邻两个sprite之间的U坐标差
		 */
		public final float stride_x;
		/**
		 * 上下相邻两个sprite之间的V坐标差
		 */
		public final float stride_y;

		public final int row;
		public final int column;

		public final float area_width;
		public final float area_height;

		public EquidistantTextureAtlas(ResourceLocation loc, int row, int column, int stride_x, int stride_y, int begin_offset_x, int begin_offset_y, int end_offset_x, int end_offset_y) {
			super(loc);
			this.row = row;
			this.column = column;
			this.stride_x = super.atlas.normalizedX(stride_x);
			this.stride_y = super.atlas.normalizedY(stride_y);
			this.begin_offset_x = super.atlas.normalizedX(begin_offset_x);
			this.begin_offset_y = super.atlas.normalizedY(begin_offset_y);
			this.end_offset_x = super.atlas.normalizedX(end_offset_x);
			this.end_offset_y = super.atlas.normalizedY(end_offset_y);
			this.area_width = (1.0f + this.end_offset_x - this.begin_offset_x - this.stride_x * (column - 1)) / column;
			this.area_height = (1.0f + this.end_offset_y - this.begin_offset_y - this.stride_y * (row - 1)) / row;
		}

		public EquidistantTextureAtlas(String loc, int row, int column, int stride_x, int stride_y, int begin_offset_x, int begin_offset_y, int end_offset_x, int end_offset_y) {
			this(ResourceLocations.build(loc), row, column, stride_x, stride_y, begin_offset_x, begin_offset_y, end_offset_x, end_offset_y);
		}

		public EquidistantTextureAtlas(ResourceLocation loc, int row, int column, int stride_x, int stride_y) {
			this(loc, row, column, stride_x, stride_y, 0, 0, 0, 0);
		}

		public EquidistantTextureAtlas(String loc, int row, int column, int stride_x, int stride_y) {
			this(loc, row, column, stride_x, stride_y, 0, 0, 0, 0);
		}

		public EquidistantTextureAtlas(ResourceLocation loc, int row, int column) {
			this(loc, row, column, 0, 0);
		}

		public EquidistantTextureAtlas(String loc, int row, int column) {
			this(loc, row, column, 0, 0);
		}

		public EquidistantTextureAtlas(ResourceLocation loc) {
			this(loc, 1, 1);
		}

		public EquidistantTextureAtlas(String loc) {
			this(loc, 1, 1);
		}

		public Texture clip(int idx_x, int idx_y) {
			if (idx_x >= 0 && idx_x < column && idx_y > 0 && idx_y < row)
				return super.clip(begin_offset_x + (area_width + stride_x) * idx_x, begin_offset_y + (area_height + stride_y) * idx_y, area_width, area_height);
			else
				return null;
		}
	}
}
